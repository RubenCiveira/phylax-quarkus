#!/bin/bash

docker_build() {
	local FILE=$1
  	docker build --platform linux/${CURRENT_PLATFORM} --progress=plain --no-cache --force-rm -f ${FILE} -t "${IMAGE_REGISTRY}/${IMAGE_NAME}:${CURRENT_VERSION}-${CURRENT_PLATFORM}" .
}

docker_publish() {
	docker login -u ${REGISTRY_USER} -p ${REGISTRY_PASS} ${IMAGE_REGISTRY} 
	docker push ${IMAGE_REGISTRY}/${IMAGE_NAME}:${CURRENT_VERSION}-${CURRENT_PLATFORM}

	if check_all_architectures_published "${IMAGE_REGISTRY}/${IMAGE_NAME}:${CURRENT_VERSION}"; then
	  echo "🚀 Lanzando docker manifest create..."
	  docker manifest create "$IMAGE_NAME" \
	    --amend "$IMAGE_NAME-amd64" \
	    --amend "$IMAGE_NAME-arm64"
	  docker manifest push "$IMAGE_NAME"
	else
	  echo "⏳ Esperando a que se publiquen todas las arquitecturas."
	fi
}

check_all_architectures_published() {
  local image_base="$1"
  local archs=("amd64" "arm64")
  local all_found=true

  for arch in "${archs[@]}"; do
    local image="${image_base}-${arch}"
    echo "🔍 Comprobando: $image"
    
    if docker manifest inspect "$image" > /dev/null 2>&1; then
      echo "✅ Encontrada: $image"
    else
      echo "❌ No encontrada: $image"
      all_found=false
    fi
  done

  if $all_found; then
    echo "✅ Todas las arquitecturas están disponibles"
    return 0
  else
    echo "⚠️ Faltan imágenes para alguna arquitectura"
    return 1
  fi
}