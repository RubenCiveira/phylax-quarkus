/*
 * Observability viewer: logs, traces and sources of /observe/api.
 * Plain ES module, no build and no CDN. The login comes from observe/config/viewer-config.json
 * (identity.main.type: oidc | token | none), with the same keys as the config of the Angular front.
 */
const root = document.getElementById('obs-root');
const BASE = '/observe';
const RANGES = { '15m': 15, '1h': 60, '6h': 360, '24h': 1440, '7d': 10080 };
const LEVELS = ['TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL'];
const store = sessionStore();
const state = { config: null, me: null, sources: [], range: store.get('obs.range') || '1h', src: '' };

/* ------------------------------------------------------------------ utilities */

function sessionStore() {
  return {
    get(key) { try { return sessionStorage.getItem(key); } catch { return null; } },
    set(key, value) { try { sessionStorage.setItem(key, value); } catch { /* no storage */ } },
    remove(key) { try { sessionStorage.removeItem(key); } catch { /* no storage */ } },
  };
}

function h(tag, attrs = {}, ...children) {
  const el = document.createElement(tag);
  for (const [key, value] of Object.entries(attrs || {})) {
    if (value === null || value === undefined || value === false) continue;
    if (key.startsWith('on')) el.addEventListener(key.substring(2), value);
    else if (key === 'class') el.className = value;
    else el.setAttribute(key, value === true ? '' : value);
  }
  for (const child of children.flat()) {
    if (child === null || child === undefined || child === false) continue;
    el.append(child instanceof Node ? child : document.createTextNode(String(child)));
  }
  return el;
}

function fmtTime(ts) {
  const d = new Date(ts);
  const today = new Date().toDateString() === d.toDateString();
  return today ? d.toLocaleTimeString() : d.toLocaleString();
}

function fmtDuration(ms) {
  if (ms < 1) return `${(ms * 1000).toFixed(0)} µs`;
  if (ms < 1000) return `${ms.toFixed(ms < 10 ? 1 : 0)} ms`;
  if (ms < 60000) return `${(ms / 1000).toFixed(2)} s`;
  return `${(ms / 60000).toFixed(1)} min`;
}

function range() {
  const to = Date.now() + 60000;
  return { from: to - RANGES[state.range] * 60000 - 60000, to };
}

function query(params) {
  const search = new URLSearchParams();
  for (const [key, value] of Object.entries(params)) {
    if (value === undefined || value === null || value === '') continue;
    search.append(key, value);
  }
  return search.toString();
}

/* ------------------------------------------------------------------ identity providers */

function base64url(bytes) {
  return btoa(String.fromCharCode(...new Uint8Array(bytes))).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}

function random(size = 32) {
  const bytes = new Uint8Array(size);
  crypto.getRandomValues(bytes);
  return base64url(bytes);
}

const providers = [
  {
    accept: (type) => type === 'oidc',
    missing(conf) { return ['issuer', 'client-id'].filter((key) => !conf[key]); },
    discoveryUrl(conf) {
      const base = (conf.url || conf.issuer).replace(/\/$/, '');
      // identity.main.url is the discovery document; an issuer given there is completed
      return base.includes('/.well-known/') ? base : `${base}/.well-known/openid-configuration`;
    },
    async discovery(conf) {
      const url = this.discoveryUrl(conf);
      let res;
      try {
        res = await fetch(url);
      } catch (error) {
        throw new Error(`The identity provider cannot be reached at ${url} (${error.message}).`);
      }
      if (!res.ok) {
        throw new Error(`The identity provider answered ${res.status} for its discovery document ${url}. `
          + 'Check identity.main.issuer and identity.main.url in observe/config/viewer-config.json.');
      }
      return res.json();
    },
    redirectUri(conf) { return conf.redirect || `${location.origin}${BASE}/callback`; },
    async login(conf, prompt) {
      const meta = await this.discovery(conf);
      const verifier = random(48);
      const challenge = base64url(await crypto.subtle.digest('SHA-256', new TextEncoder().encode(verifier)));
      const stateValue = random(16);
      store.set('obs.pkce', JSON.stringify({ verifier, state: stateValue, back: location.hash || '#/logs' }));
      const audience = Array.isArray(conf.audience) ? conf.audience.join(' ') : conf.audience;
      location.assign(`${meta.authorization_endpoint}?${query({
        response_type: 'code', client_id: conf['client-id'], redirect_uri: this.redirectUri(conf),
        scope: conf.scope || 'openid profile', state: stateValue, code_challenge: challenge,
        code_challenge_method: 'S256', audience, prompt,
      })}`);
      return new Promise(() => {});
    },
    async callback(conf) {
      const params = new URLSearchParams(location.search);
      const saved = JSON.parse(store.get('obs.pkce') || '{}');
      store.remove('obs.pkce');
      if (params.get('error')) throw new Error(params.get('error_description') || params.get('error'));
      if (!params.get('code') || params.get('state') !== saved.state) throw new Error('Invalid login response');
      const meta = await this.discovery(conf);
      await this.token(meta, {
        grant_type: 'authorization_code', code: params.get('code'), redirect_uri: this.redirectUri(conf),
        client_id: conf['client-id'], code_verifier: saved.verifier,
      });
      history.replaceState(null, '', `${BASE}/${saved.back || '#/logs'}`);
    },
    async token(meta, form) {
      const res = await fetch(meta.token_endpoint, {
        method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: query(form),
      });
      if (!res.ok) throw new Error(`token ${res.status}`);
      const body = await res.json();
      store.set('obs.token', body.access_token);
      if (body.refresh_token) store.set('obs.refresh', body.refresh_token);
      if (body.id_token) store.set('obs.id', body.id_token);
    },
    async refresh(conf) {
      const refresh = store.get('obs.refresh');
      if (!refresh) return false;
      try {
        const meta = await this.discovery(conf);
        await this.token(meta, { grant_type: 'refresh_token', refresh_token: refresh, client_id: conf['client-id'] });
        return true;
      } catch { return false; }
    },
    async logout(conf) {
      const id = store.get('obs.id');
      ['obs.token', 'obs.refresh', 'obs.id'].forEach((key) => store.remove(key));
      try {
        const meta = await this.discovery(conf);
        if (meta.end_session_endpoint) {
          location.assign(`${meta.end_session_endpoint}?${query({
            id_token_hint: id, client_id: conf['client-id'],
            post_logout_redirect_uri: conf['logout-redirect'] || `${location.origin}${BASE}/`,
          })}`);
          return;
        }
      } catch { /* go home */ }
      location.assign(`${BASE}/`);
    },
  },
  {
    accept: (type) => type === 'token',
    missing() { return []; },
    async login() {
      return new Promise((resolve) => {
        const input = h('input', { type: 'password', autocomplete: 'off', 'aria-label': 'Access token' });
        gate('Access token', [h('p', { class: 'obs-muted' }, 'This viewer is protected by an access token.'), input],
          [h('button', { class: 'primary', onclick: () => { store.set('obs.token', input.value.trim()); resolve(); } }, 'Sign in')]);
        input.focus();
      });
    },
    async refresh() { return false; },
    async logout() { store.remove('obs.token'); location.assign(`${BASE}/`); },
  },
  {
    accept: (type) => type === 'none',
    missing() { return []; },
    async login() {},
    async refresh() { return false; },
    async logout() { location.assign(`${BASE}/`); },
  },
];

function identity() {
  return state.config?.identity?.main || {};
}

function provider() {
  return providers.find((p) => p.accept(identity().type || ''));
}

/* ------------------------------------------------------------------ api */

async function api(path, params = {}, retry = true) {
  const headers = {};
  const token = store.get('obs.token');
  if (token) headers.Authorization = `Bearer ${token}`;
  const apiUrl = state.config?.api?.url || `${BASE}/api`;
  const suffix = Object.keys(params).length ? `?${query(params)}` : '';
  const res = await fetch(`${apiUrl}${path}${suffix}`, { headers });
  if (res.status === 401 && retry && await provider().refresh(identity())) {
    return api(path, params, false);
  }
  if (!res.ok) {
    let reason = '';
    try { reason = (await res.json()).reason || ''; } catch { /* no body */ }
    const error = new Error(reason || `HTTP ${res.status}`);
    error.status = res.status;
    error.reason = reason;
    if (res.status === 401 && state.me) {
      // The session expired while using the viewer and cannot be refreshed: sign in again.
      store.remove('obs.token');
      gate('Session expired', [h('p', {}, 'Sign in again to continue.')],
        [h('button', { class: 'primary', onclick: () => signIn().catch(() => {}) }, 'Sign in')]);
    }
    throw error;
  }
  return res.json();
}

/* ------------------------------------------------------------------ gate */

/** Starts the sign in; a failure (discovery, network...) is shown, never left in the console. */
async function signIn(prompt) {
  try {
    await provider().login(identity(), prompt);
  } catch (error) {
    gate('Unable to sign in', [h('p', {}, error.message || String(error))],
      [h('button', { class: 'primary', onclick: () => signIn(prompt) }, 'Retry')]);
    throw error;
  }
}

function gate(title, body, actions = []) {
  root.replaceChildren(h('div', { class: 'obs-gate' },
    h('div', { class: 'obs-card', role: 'dialog', 'aria-labelledby': 'obs-gate-title' },
      h('h1', { id: 'obs-gate-title' }, title), ...body, h('div', {}, ...actions))));
}

async function start() {
  root.replaceChildren(h('div', { class: 'obs-loading', role: 'progressbar', 'aria-label': 'Loading' }));
  try {
    const res = await fetch(`${BASE}/config/viewer-config.json`, { cache: 'no-store' });
    state.config = await res.json();
  } catch {
    gate('Login is not configured', [h('p', {}, 'observe/config/viewer-config.json cannot be read.')]);
    return;
  }
  const conf = identity();
  const prov = provider();
  if (!prov) {
    gate('Login is not configured', [h('p', {}, `Unknown identity.main.type "${conf.type || ''}" in viewer-config.json.`)]);
    return;
  }
  const missing = prov.missing(conf);
  if (missing.length) {
    gate('Login is not configured', [h('p', {}, 'Missing keys in observe/config/viewer-config.json:'),
      h('ul', {}, missing.map((key) => h('li', { class: 'obs-mono' }, `identity.main.${key}`)))]);
    return;
  }
  try {
    if (location.pathname.endsWith('/callback')) {
      gate('Signing in…', [h('div', { class: 'obs-loading' })]);
      await prov.callback(conf);
    }
  } catch (error) {
    gate('Unable to sign in', [h('p', { class: 'obs-muted' }, error.message)],
      [h('button', { class: 'primary', onclick: () => signIn().catch(() => {}) }, 'Retry')]);
    return;
  }
  for (let attempt = 0; attempt < 2; attempt++) {
    try {
      state.me = await api('/me');
      break;
    } catch (error) {
      if (error.status === 401 && attempt === 0) {
        if (conf.type === 'oidc') gate('Redirecting to sign in…', [h('div', { class: 'obs-loading' })]);
        try {
          await signIn();
        } catch {
          return;
        }
        continue;
      }
      if (error.status === 403 || error.status === 401) {
        gate('No access', [h('p', {}, 'Your user has no access to the observability.'),
          error.reason ? h('p', { class: 'obs-muted' }, error.reason) : null],
          [h('button', { onclick: () => prov.logout(conf) }, 'Sign out'), ' ',
            conf.type === 'oidc' ? h('button', { onclick: () => signIn('login').catch(() => {}) }, 'Change user') : null]);
        return;
      }
      gate('Unable to reach the server', [h('p', { class: 'obs-muted' }, error.message)],
        [h('button', { class: 'primary', onclick: start }, 'Retry')]);
      return;
    }
  }
  try { state.sources = await api('/sources'); } catch { state.sources = []; }
  shell();
  addEventListener('hashchange', render);
  render();
}

/* ------------------------------------------------------------------ shell */

let main;
let tabs;

function shell() {
  const title = state.config?.meta?.title || root.dataset.title || 'observe';
  const rangeSelect = h('select', { 'aria-label': 'Time range', onchange: (e) => { state.range = e.target.value; store.set('obs.range', state.range); render(); } },
    Object.keys(RANGES).map((key) => h('option', { value: key, selected: key === state.range }, `Last ${key}`)));
  const sourceSelect = h('select', { 'aria-label': 'Source', onchange: (e) => { state.src = e.target.value; render(); } },
    h('option', { value: '' }, 'All sources'),
    state.sources.map((s) => h('option', { value: s.code }, s.name || s.code)));
  tabs = h('nav', { class: 'obs-tabs', 'aria-label': 'Sections' },
    h('a', { href: '#/logs' }, 'Logs'), h('a', { href: '#/traces' }, 'Traces'), h('a', { href: '#/sources' }, 'Sources'));
  const levels = (state.me.levels || []).includes('ADMIN') ? 'Admin' : (state.me.levels || []).includes('WRITE') ? 'Edit' : 'Read';
  main = h('main', { class: 'obs-main', id: 'obs-main' });
  root.replaceChildren(
    h('header', { class: 'obs-bar' },
      h('span', { class: 'obs-brand' }, title), tabs, h('span', { class: 'obs-spacer' }),
      sourceSelect, rangeSelect, h('button', { onclick: render, title: 'Refresh (r)' }, '⟳'),
      h('span', { class: 'obs-user' }, `${state.me.user} · `, h('span', { class: 'obs-chip' }, levels)),
      h('button', { onclick: () => provider().logout(identity()) }, 'Sign out')),
    main);
  addEventListener('keydown', (e) => {
    if (e.key === 'r' && !['INPUT', 'SELECT', 'TEXTAREA'].includes(document.activeElement?.tagName)) render();
  });
}

function render() {
  const hash = location.hash || '#/logs';
  for (const a of tabs.querySelectorAll('a')) {
    if (hash.startsWith(a.getAttribute('href'))) a.setAttribute('aria-current', 'page');
    else a.removeAttribute('aria-current');
  }
  const [, view, id] = hash.split('/');
  if (view === 'traces' && id) return traceView(id);
  if (view === 'traces') return tracesView();
  if (view === 'sources') return sourcesView();
  return logsView();
}

async function load(build, fetcher) {
  main.replaceChildren(h('div', { class: 'obs-loading' }));
  try {
    main.replaceChildren(...[build(await fetcher())].flat());
  } catch (error) {
    main.replaceChildren(h('div', { class: 'obs-error-band', role: 'alert' }, `Unable to load: ${error.message} `,
      h('button', { onclick: render }, 'Retry')));
  }
}

function empty(text) {
  return h('div', { class: 'obs-empty' }, text);
}

/* ------------------------------------------------------------------ logs */

const logFilter = { level: 'INFO', q: '' };

function logsView() {
  const filters = h('form', { class: 'obs-filters', onsubmit: (e) => { e.preventDefault(); logFilter.q = e.target.q.value; logFilter.level = e.target.level.value; render(); } },
    h('select', { name: 'level', 'aria-label': 'Minimum level' }, LEVELS.map((l) => h('option', { value: l, selected: l === logFilter.level }, `≥ ${l}`))),
    h('input', { type: 'search', name: 'q', value: logFilter.q, placeholder: 'Search in message, logger, attributes', 'aria-label': 'Search' }),
    h('button', { class: 'primary', type: 'submit' }, 'Search'));
  load((logs) => [filters, logs.length ? logTable(logs) : empty('No logs in this range')],
    () => api('/logs', { src: state.src, level: logFilter.level, q: logFilter.q, ...range(), limit: 500 }));
}

function logTable(logs) {
  const body = h('tbody');
  for (const log of logs) {
    const detail = h('tr', { hidden: true }, h('td', { colspan: 5, class: 'obs-detail' },
      log.error ? `${log.error}\n` : '', JSON.stringify(log.attrs || {}, null, 2),
      log.traceId ? h('div', {}, h('a', { href: `#/traces/${log.traceId}` }, `Trace ${log.traceId}`)) : null));
    body.append(h('tr', { class: 'obs-row', tabindex: 0, onclick: () => { detail.hidden = !detail.hidden; }, onkeydown: (e) => { if (e.key === 'Enter') detail.hidden = !detail.hidden; } },
      h('td', { class: 'obs-mono obs-muted' }, fmtTime(log.ts)),
      h('td', {}, h('span', { class: `obs-chip lvl-${log.level}` }, log.level)),
      h('td', { class: 'obs-hide-sm' }, log.src),
      h('td', { class: 'obs-mono obs-muted obs-hide-sm' }, (log.logger || '').split('.').pop()),
      h('td', { class: 'obs-msg' }, log.msg, log.error ? h('span', { class: 'obs-err' }, ' ⚠') : null)), detail);
  }
  return h('table', { class: 'obs-table' },
    h('thead', {}, h('tr', {}, h('th', {}, 'Time'), h('th', {}, 'Level'), h('th', { class: 'obs-hide-sm' }, 'Source'),
      h('th', { class: 'obs-hide-sm' }, 'Logger'), h('th', {}, 'Message'))), body);
}

/* ------------------------------------------------------------------ traces */

const traceFilter = { q: '', errors: false };

function tracesView() {
  const filters = h('form', { class: 'obs-filters', onsubmit: (e) => { e.preventDefault(); traceFilter.q = e.target.q.value; traceFilter.errors = e.target.errors.checked; render(); } },
    h('input', { type: 'search', name: 'q', value: traceFilter.q, placeholder: 'Search operation or attributes', 'aria-label': 'Search' }),
    h('label', {}, h('input', { type: 'checkbox', name: 'errors', checked: traceFilter.errors }), ' Only errors'),
    h('button', { class: 'primary', type: 'submit' }, 'Search'));
  load((traces) => [filters, traces.length ? traceTable(traces) : empty('No traces in this range')],
    () => api('/traces', { src: state.src, q: traceFilter.q, errors: traceFilter.errors || '', ...range(), limit: 200 }));
}

function traceTable(traces) {
  return h('table', { class: 'obs-table' },
    h('thead', {}, h('tr', {}, h('th', {}, 'Time'), h('th', {}, 'Operation'), h('th', { class: 'obs-hide-sm' }, 'Sources'),
      h('th', {}, 'Duration'), h('th', { class: 'obs-hide-sm' }, 'Spans'))),
    h('tbody', {}, traces.map((t) => h('tr', { class: 'obs-row', tabindex: 0, onclick: () => { location.hash = `#/traces/${t.traceId}`; }, onkeydown: (e) => { if (e.key === 'Enter') location.hash = `#/traces/${t.traceId}`; } },
      h('td', { class: 'obs-mono obs-muted' }, fmtTime(t.ts)),
      h('td', {}, t.error ? h('span', { class: 'obs-err' }, '● ') : h('span', { class: 'obs-ok' }, '● '), t.name),
      h('td', { class: 'obs-hide-sm obs-muted' }, (t.sources || []).join(', ')),
      h('td', { class: 'obs-mono' }, fmtDuration(t.durMs)),
      h('td', { class: 'obs-hide-sm obs-mono' }, t.spans)))));
}

function traceView(traceId) {
  load((trace) => {
    if (!trace.spans.length) return [h('a', { href: '#/traces' }, '← Traces'), empty('Trace not found in the last 24 hours')];
    const start = Math.min(...trace.spans.map((s) => s.ts));
    const end = Math.max(...trace.spans.map((s) => s.ts + s.durMs));
    const total = Math.max(end - start, 0.001);
    const children = new Map();
    for (const span of trace.spans) {
      const parent = trace.spans.some((s) => s.spanId === span.parentId) ? span.parentId : '';
      if (!children.has(parent)) children.set(parent, []);
      children.get(parent).push(span);
    }
    const rows = [];
    const walk = (parent, depth) => {
      for (const span of (children.get(parent) || []).sort((a, b) => a.ts - b.ts)) {
        const left = ((span.ts - start) / total) * 100;
        const width = Math.max((span.durMs / total) * 100, 0.2);
        rows.push(h('div', { class: 'obs-wf-name', style: `padding-left:${8 + depth * 14}px`, title: JSON.stringify(span.attrs || {}, null, 2) },
          span.isError || span.status === 'ERROR' ? h('span', { class: 'obs-err' }, '● ') : null,
          h('strong', {}, span.src), ' ', span.name),
        h('div', { class: 'obs-wf-track' },
          h('div', { class: `obs-wf-bar${span.status === 'ERROR' ? ' error' : ''}`, style: `left:${left}%;width:${width}%` }),
          h('div', { class: 'obs-wf-label', style: `left:${Math.min(left, 85)}%` }, fmtDuration(span.durMs))));
        walk(span.spanId, depth + 1);
      }
    };
    walk('', 0);
    return [
      h('p', {}, h('a', { href: '#/traces' }, '← Traces'), ' ', h('span', { class: 'obs-mono obs-muted' }, traceId),
        ' · ', fmtDuration(total), ' · ', `${trace.spans.length} spans`),
      h('div', { class: 'obs-waterfall', role: 'table', 'aria-label': 'Spans of the trace' }, rows),
      h('h2', {}, 'Logs of the trace'),
      trace.logs.length ? logTable(trace.logs) : empty('No logs for this trace'),
    ];
  }, () => api(`/traces/${encodeURIComponent(traceId)}`));
}

/* ------------------------------------------------------------------ sources */

function sourcesView() {
  load((sources) => sources.length ? h('table', { class: 'obs-table' },
    h('thead', {}, h('tr', {}, h('th', {}, 'Code'), h('th', {}, 'Name'), h('th', { class: 'obs-hide-sm' }, 'Type'),
      h('th', {}, 'State'), h('th', {}, 'Last event'), h('th', { class: 'obs-hide-sm' }, 'Ingest key'))),
    h('tbody', {}, sources.map((s) => h('tr', {},
      h('td', { class: 'obs-mono' }, s.code), h('td', {}, s.name),
      h('td', { class: 'obs-hide-sm obs-muted' }, s.originType),
      h('td', {}, s.accepting ? h('span', { class: 'obs-ok' }, 'Accepting') : h('span', { class: 'obs-muted' }, 'Rejected')),
      h('td', { class: 'obs-muted' }, s.lastSeen ? fmtTime(s.lastSeen) : '—'),
      h('td', { class: 'obs-hide-sm obs-mono' }, s.ingestKey ? h('code', {}, s.ingestKey) : h('span', { class: 'obs-muted' }, 'Admin only'))))))
    : empty('No sources'), async () => { state.sources = await api('/sources'); return state.sources; });
}

// Last resort: an error nobody handled is shown instead of being lost in the console.
addEventListener('unhandledrejection', (event) => {
  gate('Unexpected error', [h('p', { class: 'obs-muted' }, event.reason?.message || String(event.reason))],
    [h('button', { class: 'primary', onclick: () => location.reload() }, 'Reload')]);
});

start();
