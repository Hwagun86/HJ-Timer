 (cd "$(git rev-parse --show-toplevel)" && git apply --3way <<'EOF' 
diff --git a//dev/null b/README.md
index 0000000000000000000000000000000000000000..77fb40ee3f33306399076dd945862ee1e28a1165 100644
--- a//dev/null
+++ b/README.md
@@ -0,0 +1,30 @@
+# FocusTimer
+
+This repository contains the FocusTimer application built with JavaFX.
+
+## Building
+
+To build the fat JAR, run:
+
+```bash
+chmod +x gradlew
+./gradlew shadowJar
+```
+
+The resulting `build/libs/FocusTimer-all.jar` can be packaged with `jpackage`.
+
+## GitHub integration
+
+To push your changes, first create a GitHub repository and add it as a remote:
+
+```bash
+git remote add origin https://github.com/USER/REPO.git
+```
+
+Then push the current branch:
+
+```bash
+git push -u origin work
+```
+
+Replace `USER/REPO` with your repository path.
 
EOF
)
