#!/bin/bash

case "$SOURCE_KIND" in
  maven)
    source "$SCRIPT_DIR/artifact/maven.sh"
    ;;
  quarkus)
    source "$SCRIPT_DIR/artifact/quarkus.sh"
    ;;  
  npm)
    source "$SCRIPT_DIR/artifact/npm.sh"
    ;;
  composer)
    source "$SCRIPT_DIR/artifact/composer.sh"
    ;;
  *)
    echo "Tipo de proyecto no soportado: $KIND"
    exit 1
    ;;
esac

