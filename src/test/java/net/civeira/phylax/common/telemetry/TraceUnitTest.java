package net.civeira.phylax.common.telemetry;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;

@DisplayName("Trace utility class")
class TraceUnitTest {

  private Span createValidMockSpan() {
    Span mockSpan = mock(Span.class);
    SpanContext ctx = mock(SpanContext.class);
    when(ctx.isValid()).thenReturn(true);
    when(mockSpan.getSpanContext()).thenReturn(ctx);
    return mockSpan;
  }

  private Span createInvalidMockSpan() {
    Span mockSpan = mock(Span.class);
    SpanContext ctx = mock(SpanContext.class);
    when(ctx.isValid()).thenReturn(false);
    when(mockSpan.getSpanContext()).thenReturn(ctx);
    return mockSpan;
  }

  @Nested
  @DisplayName("addEvent with name only")
  class AddEventWithName {

    @Test
    @DisplayName("Should add event when span is valid and name is not blank")
    void shouldAddEventWhenSpanIsValidAndNameIsNotBlank() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a non-blank event name
        Trace.addEvent("test-event");

        // Assert — the event should be registered on the span with the given name
        verify(mockSpan,
            description("addEvent should be called with event name when span is valid"))
                .addEvent("test-event");
      }
    }

    @Test
    @DisplayName("Should not add event when name is null")
    void shouldNotAddEventWhenNameIsNull() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a null event name
        Trace.addEvent(null);

        // Assert — no event should be added to the span
        verify(mockSpan, never().description("addEvent should NOT be called when name is null"))
            .addEvent(anyString());
      }
    }

    @Test
    @DisplayName("Should not add event when name is blank")
    void shouldNotAddEventWhenNameIsBlank() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a blank event name
        Trace.addEvent("   ");

        // Assert — no event should be added to the span
        verify(mockSpan, never().description("addEvent should NOT be called when name is blank"))
            .addEvent(anyString());
      }
    }

    @Test
    @DisplayName("Should not add event when span context is invalid")
    void shouldNotAddEventWhenSpanContextIsInvalid() {
      // Arrange — an invalid mock span is created and set as the current span
      Span mockSpan = createInvalidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a valid event name but invalid span
        Trace.addEvent("event");

        // Assert — no event should be added since the span context is invalid
        verify(mockSpan, never().description("addEvent should NOT be called when span is invalid"))
            .addEvent(anyString());
      }
    }
  }

  @Nested
  @DisplayName("addEvent with name and attributes")
  class AddEventWithAttributes {

    @Test
    @DisplayName("Should add event with attributes when all params are valid")
    void shouldAddEventWithAttributesWhenValid() {
      // Arrange — a valid mock span and an attributes map with two entries are prepared
      Span mockSpan = createValidMockSpan();
      Map<String, String> attrs = Map.of("key1", "val1", "key2", "val2");

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a valid name and a non-empty attributes map
        Trace.addEvent("event", attrs);

        // Assert — the event should be registered with its associated attributes
        verify(mockSpan, description("addEvent should be called with attributes map"))
            .addEvent(eq("event"), any(Attributes.class));
      }
    }

    @Test
    @DisplayName("Should add event without attributes when map is null")
    void shouldAddEventWithoutAttributesWhenMapIsNull() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a valid name but a null attributes map
        Trace.addEvent("event", (Map<String, String>) null);

        // Assert — the event should be added without attributes as a name-only event
        verify(mockSpan,
            description("addEvent should be called without attributes when map is null"))
                .addEvent("event");
      }
    }

    @Test
    @DisplayName("Should add event without attributes when map is empty")
    void shouldAddEventWithoutAttributesWhenMapIsEmpty() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a valid name but an empty attributes map
        Trace.addEvent("event", Map.of());

        // Assert — the event should be added without attributes as a name-only event
        verify(mockSpan,
            description("addEvent should be called without attributes when map is empty"))
                .addEvent("event");
      }
    }

    @Test
    @DisplayName("Should not add event when name is null even with attributes")
    void shouldNotAddEventWhenNameIsNullEvenWithAttributes() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a null name and a non-empty attributes map
        Trace.addEvent(null, Map.of("k", "v"));

        // Assert — no event should be added regardless of provided attributes
        verify(mockSpan, never().description("addEvent should NOT be called when name is null"))
            .addEvent(anyString());
        verify(mockSpan,
            never().description("addEvent with attrs should NOT be called when name is null"))
                .addEvent(anyString(), any(Attributes.class));
      }
    }

    @Test
    @DisplayName("Should not add event when span context is invalid")
    void shouldNotAddEventWithAttributesWhenSpanInvalid() {
      // Arrange — an invalid mock span is created and set as the current span
      Span mockSpan = createInvalidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — addEvent is called with a valid name and attributes but an invalid span
        Trace.addEvent("event", Map.of("k", "v"));

        // Assert — no event should be added since the span context is invalid
        verify(mockSpan, never().description("addEvent should NOT be called when span is invalid"))
            .addEvent(anyString(), any(Attributes.class));
      }
    }
  }

  @Nested
  @DisplayName("setAttribute with boolean value")
  class SetAttributeBoolean {

    @Test
    @DisplayName("Should set boolean attribute when key is valid")
    void shouldSetBooleanAttributeWhenKeyIsValid() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a valid key and a boolean value
        Trace.setAttribute("error", true);

        // Assert — the boolean attribute should be set on the span
        verify(mockSpan, description("setAttribute(boolean) should be called"))
            .setAttribute("error", true);
      }
    }

    @Test
    @DisplayName("Should not set boolean attribute when key is null")
    void shouldNotSetBooleanAttributeWhenKeyIsNull() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a null key and a boolean value
        Trace.setAttribute(null, true);

        // Assert — no attribute should be set on the span
        verify(mockSpan,
            never().description("setAttribute(boolean) should NOT be called when key is null"))
                .setAttribute(anyString(), anyBoolean());
      }
    }

    @Test
    @DisplayName("Should not set boolean attribute when key is blank")
    void shouldNotSetBooleanAttributeWhenKeyIsBlank() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a blank key and a boolean value
        Trace.setAttribute("  ", false);

        // Assert — no attribute should be set on the span
        verify(mockSpan,
            never().description("setAttribute(boolean) should NOT be called when key is blank"))
                .setAttribute(anyString(), anyBoolean());
      }
    }
  }

  @Nested
  @DisplayName("setAttribute with double value")
  class SetAttributeDouble {

    @Test
    @DisplayName("Should set double attribute when key is valid")
    void shouldSetDoubleAttributeWhenKeyIsValid() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a valid key and a double value
        Trace.setAttribute("latency", 3.14);

        // Assert — the double attribute should be set on the span
        verify(mockSpan, description("setAttribute(double) should be called"))
            .setAttribute("latency", 3.14);
      }
    }

    @Test
    @DisplayName("Should not set double attribute when key is null")
    void shouldNotSetDoubleAttributeWhenKeyIsNull() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a null key and a double value
        Trace.setAttribute(null, 1.0);

        // Assert — no attribute should be set on the span
        verify(mockSpan,
            never().description("setAttribute(double) should NOT be called when key is null"))
                .setAttribute(anyString(), anyDouble());
      }
    }
  }

  @Nested
  @DisplayName("setAttribute with long value")
  class SetAttributeLong {

    @Test
    @DisplayName("Should set long attribute when key is valid")
    void shouldSetLongAttributeWhenKeyIsValid() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a valid key and a long value
        Trace.setAttribute("count", 42L);

        // Assert — the long attribute should be set on the span
        verify(mockSpan, description("setAttribute(long) should be called")).setAttribute("count",
            42L);
      }
    }

    @Test
    @DisplayName("Should not set long attribute when key is null")
    void shouldNotSetLongAttributeWhenKeyIsNull() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a null key and a long value
        Trace.setAttribute(null, 1L);

        // Assert — no attribute should be set on the span
        verify(mockSpan,
            never().description("setAttribute(long) should NOT be called when key is null"))
                .setAttribute(anyString(), anyLong());
      }
    }
  }

  @Nested
  @DisplayName("setAttribute with String value")
  class SetAttributeString {

    @Test
    @DisplayName("Should set string attribute when key and value are valid")
    void shouldSetStringAttributeWhenKeyAndValueAreValid() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a valid key and a valid string value
        Trace.setAttribute("user", "john");

        // Assert — the string attribute should be set on the span
        verify(mockSpan, description("setAttribute(String) should be called")).setAttribute("user",
            "john");
      }
    }

    @Test
    @DisplayName("Should not set string attribute when key is null")
    void shouldNotSetStringAttributeWhenKeyIsNull() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a null key and a string value
        Trace.setAttribute(null, "value");

        // Assert — no attribute should be set on the span
        verify(mockSpan,
            never().description("setAttribute(String) should NOT be called when key is null"))
                .setAttribute(anyString(), anyString());
      }
    }

    @Test
    @DisplayName("Should not set string attribute when value is null")
    void shouldNotSetStringAttributeWhenValueIsNull() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a valid key but a null string value
        Trace.setAttribute("key", (String) null);

        // Assert — no attribute should be set on the span
        verify(mockSpan,
            never().description("setAttribute(String) should NOT be called when value is null"))
                .setAttribute(anyString(), anyString());
      }
    }

    @Test
    @DisplayName("Should not set string attribute when key is blank")
    void shouldNotSetStringAttributeWhenKeyIsBlank() {
      // Arrange — a valid mock span is created and set as the current span
      Span mockSpan = createValidMockSpan();

      try (MockedStatic<Span> spanStatic = mockStatic(Span.class)) {
        spanStatic.when(Span::current).thenReturn(mockSpan);

        // Act — setAttribute is called with a blank key and a string value
        Trace.setAttribute("", "value");

        // Assert — no attribute should be set on the span
        verify(mockSpan,
            never().description("setAttribute(String) should NOT be called when key is blank"))
                .setAttribute(anyString(), anyString());
      }
    }
  }
}
