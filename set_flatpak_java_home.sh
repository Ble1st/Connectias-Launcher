#!/bin/bash

# WARNING: It is generally not recommended to set JAVA_HOME to a Flatpak-internal JDK
# due to isolation design and potential compatibility issues with other system applications.
# Use this script only if you understand the implications and for temporary session-specific use.

FLATPAK_JAVA_HOME="/var/lib/flatpak/app/com.google.AndroidStudio/x86_64/stable/084908a2567ee8fb79ff0300feeedf16a86c552eff41b12d85414dc3516d2e3e/files/extra/jbr"

if [ -d "$FLATPAK_JAVA_HOME" ]; then
    echo "Setting JAVA_HOME to Flatpak's JBR: $FLATPAK_JAVA_HOME"
    export JAVA_HOME="$FLATPAK_JAVA_HOME"
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "JAVA_HOME is now: $JAVA_HOME"
    echo "Java version:"
    java -version
else
    echo "Error: Flatpak Java Home not found at $FLATPAK_JAVA_HOME"
    echo "Please verify the path or install a system-wide JDK."
fi
