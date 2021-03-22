package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;

public class Controller {

    @FXML
    private TextField inputRegisterStateTextField;

    @FXML
    private TextArea generatedKeyBitsTextArea;

    @FXML
    private TextArea inputFileBitsTextArea;

    @FXML
    private TextArea resultBitsTextArea;

    @FXML
    void initialize() {
        addTextLimiter(inputRegisterStateTextField, 29);
    }

    @FXML
    void saveFile() {
        setContentToFile();
    }

    @FXML
    void openFile() {
        getFileContent();
    }


    @FXML
    void encryptDecrypt() {
        encryptDecryptMethod();
    }


    private void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }


    private void encryptDecryptMethod() {
        String keyState = getOnlyBits(inputRegisterStateTextField.getText());
        if (keyState.length() != 29) {
            showAlertMessage("Incorrect input of the key register state", "The length of the register blocks is not equal 29");
        } else {
            generatedKeyBitsTextArea.clear();
            StringBuilder keyBits = generateKeyBits(keyState);
            encryptDecryptFileBits(keyBits);
        }
    }


    private void encryptDecryptFileBits(StringBuilder key) {
        resultBitsTextArea.clear();

        StringBuilder cipherBits = LFSR.encryptDecryptFileBits(key, inputFileBitsTextArea.getText());
        resultBitsTextArea.setText(cipherBits.toString());
    }


    private StringBuilder generateKeyBits(String keyState) {
        StringBuilder keyBits = LFSR.generateKeyBits(keyState, inputFileBitsTextArea.getText());

        generatedKeyBitsTextArea.setText(keyBits.toString());
        return keyBits;
    }


    private void getFileContent() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            inputFileBitsTextArea.clear();

            StringBuilder sbBinaryFile = new StringBuilder();
            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                for (int b; (b = inputStream.read()) != -1; ) {
                    String s = "0000000" + Integer.toBinaryString(b);
                    s = s.substring(s.length() - 8);
                    sbBinaryFile.append(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputFileBitsTextArea.setText(sbBinaryFile.toString());
        }

    }

    private void setContentToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*."));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                StringBuilder tempSB = new StringBuilder();
                String resBitsText = resultBitsTextArea.getText();
                for (int i = 0; i < resBitsText.length(); i++) {
                    tempSB.append(resBitsText.charAt(i));
                    if (tempSB.length() == 8) {
                        Scanner sc = new Scanner(tempSB.toString());
                        while (sc.hasNextInt()) {
                            int b = sc.nextInt(2);
                            outputStream.write(b);
                        }
                        tempSB = new StringBuilder();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showAlertMessage(String titleOfAlert, String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titleOfAlert);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }


    private String getOnlyBits(String inputKeyState) {
        StringBuilder keyState = new StringBuilder();
        for (int i = 0; i < inputKeyState.length(); i++) {
            if (inputKeyState.charAt(i) == '1' || inputKeyState.charAt(i) == '0') {
                keyState.append(inputKeyState.charAt(i));
            }
        }
        return keyState.toString();
    }

}
