export FX_PATH="lib"
export CP="bin:lib/gson-2.13.2.jar"

# Compile
javac --module-path $FX_PATH \
      --add-modules javafx.controls,javafx.fxml,itextpdf \
      -cp $CP \
      -d bin \
      src/services/*.java src/gui/*.java src/Main.java

# Copy FXML dan resources ke bin
cp -r src/gui bin/
cp -r src/data bin/

# Pack JAR (untuk Scene Builder)
jar cf app.jar -C bin .

# Run
java --module-path $FX_PATH \
     --add-modules javafx.controls,javafx.fxml,itextpdf \
     -cp $CP \
     Main