#!/bin/bash

copy_and_prepare_github_docs() {
	local base_input_dir="src/main/docs"
	local base_output_dir="."
	local attrs_file="src/main/docs/site-attrs.adoc"
	local default_lang="en"

	# Archivos que deben ir al root del repo
	local -a publish_at_root=("README.md" "CODE_OF_CONDUCT.md" "CONTRIBUTING.md")

	# Crear script sed con atributos
	local sed_script
	sed_script=$(mktemp)
	while IFS= read -r line; do
		if [[ "$line" =~ ^:([^:]+):[[:space:]]*(.+)$ ]]; then
			key="${BASH_REMATCH[1]}"
			value="${BASH_REMATCH[2]}"
			escaped_value=$(printf '%s\n' "$value" | sed -e 's/[\/&]/\\&/g')
			echo "s/{{${key}}}/$escaped_value/g" >> "$sed_script"
		fi
	done < "$attrs_file"

	# Procesar idiomas
	for lang_dir in "$base_input_dir"/*/; do
		lang=$(basename "$lang_dir")
		echo "🌐 Procesando idioma: $lang"
		local output_lang_dir="$base_output_dir/docs/$lang"

		# Si es el idioma por defecto, crear docs/es y preparar root
		if [[ "$lang" == "$default_lang" ]]; then
			mkdir -p "$output_lang_dir"
		else
			mkdir -p "$output_lang_dir"
		fi

		find "$lang_dir/site" -type f -name '*.md' | while read -r file; do
			local filename
			filename=$(basename "$file")

			# Ruta de salida
			local output_file
			if [[ "$lang" == "$default_lang" && " ${publish_at_root[*]} " == *" $filename "* ]]; then
				output_file="$base_output_dir/$filename"
			else
				output_file="$output_lang_dir/$filename"
			fi

			# Copiar y aplicar sed con reemplazos
			cp "$file" "$output_file"
			sed -i '' -f "$sed_script" "$output_file"

			# Ajustar enlaces relativos según el idioma
			if [[ "$lang" == "$default_lang" ]]; then
				# En idioma por defecto: ../archivo → ./archivo
				sed -i '' -E 's|\.\./([^/][^)]*)|./docs/\1|g' "$output_file"
			else
				# En idiomas secundarios: ../en/archivo → ../../archivo
				sed -i '' -E "s|\.\./$default_lang/([^)]*)|../../\1|g" "$output_file"
			fi

			echo "📄 $lang: $filename → $(realpath "$output_file")"
		done
	done

	rm -f "$sed_script"
	echo "✅ Documentación multilingüe preparada para GitHub Pages."
}
