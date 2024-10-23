package com.example.calculate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calculate.utils.AppConstant;
import com.example.calculate.utils.TestUtils;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView displayTextView;  // textview для цифр
    private TextView displayHistoryTextView;
    private String displayText;  // цифры на дисплее
    private String displayHistoryText;  // цифры на дисплее
    private String operation;  // последняя операция
    private float result;  // текущий результат
    private boolean isLastOperation;  // последнее действие это какая то операция


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        displayTextView = findViewById(R.id.display_text_view);
        displayHistoryTextView = findViewById(R.id.display_history_text_view);
        clearCalculate();

        setListenerForButtons();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button btn = (Button) v;
            String btnText = btn.getText().toString();

            // очистить
            if (btnText.equals("C")) {
                clearCalculate();
            }
            // удалить
            else if (btnText.equals("←") && !Objects.equals(displayText, "")) {
                if (isLastOperation) {
                    return;
                }
                deleteDisplaySymbol();
            }

            // равно
            else if (btnText.equals("=")) {
                float currNum = TestUtils.convertStrToFloat(displayText);
                calculateOperation(currNum, operation);

                isLastOperation = false;
                operation = "";

                displayHistoryText += " " + TestUtils.getStrForDisplayByNumber(currNum) + " =";
            }

            // унарная операция
            else if (TestUtils.isOperation(btnText, AppConstant.UNARY_OPERATIONS)) {
                float currNum = TestUtils.convertStrToFloat(displayText);
                calculateUnaryOperation(currNum, btnText);
            }

            // операция
            else if (TestUtils.isOperation(btnText, AppConstant.OPERATIONS)) {
                float currNum = TestUtils.convertStrToFloat(displayText);
                if (Objects.equals(operation, "")) {  // если до этого не было операций
                    result = currNum;
                }
                else if (!isLastOperation) {  // если последнее действие не было операцией
                    calculateOperation(currNum, operation);
                }
                operation = btnText;
                isLastOperation = true;
                displayHistoryText = TestUtils.getStrForDisplayByNumber(result) + " " + operation;
            }

            // точка/цифра
            else {
                // точка
                if (btnText.equals(".")) {
                    if (isLastOperation) {
                        displayText = "0" + btnText;
                    }
                    else if (!displayText.contains(btnText)) {
                        displayText += btnText;
                    }
                }

                // цифра
                else {
                    if (isLastOperation) {
                        displayText = btnText;
                    }
                    else {
                        displayText = (Objects.equals(displayText, "0")) ? btnText : displayText + btnText;
                    }
                }
                isLastOperation = false;
            }

            displayHistoryTextView.setText(displayHistoryText);
            displayTextView.setText(displayText);
        }
    }


    private void calculateOperation(float currNum, String operation) {
        switch (operation) {
            case "+":
                result += currNum;
                break;
            case "-":
                result -= currNum;
                break;
            case "x":
                result *= currNum;
                break;
            case "/":
                if (currNum == 0) {
                    Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                }
                else {
                    result /= currNum;
                }
                break;
            case "x²":
                result = (float)Math.pow(currNum, 2);
                break;
            case "+/-":
                result *= -1;
                break;
            default:
                Toast.makeText(this, "No such operation exists...", Toast.LENGTH_SHORT).show();
        }
        result = TestUtils.roundNumber(result, 3);
        displayText = TestUtils.getStrForDisplayByNumber(result);
    }

    private void calculateUnaryOperation(float currNum, String operation) {
        switch (operation) {
            case "x²":
                currNum = (float)Math.pow(currNum, 2);
                break;
            case "+/-":
                currNum *= -1;
                break;
            default:
                Toast.makeText(this, "No such operation exists...", Toast.LENGTH_SHORT).show();
        }
        displayText = TestUtils.getStrForDisplayByNumber(currNum);
    }

    private void deleteDisplaySymbol() {
        displayText = displayText.substring(0, displayText.length() - 1);
        if (Objects.equals(displayText, "") || Objects.equals(displayText, "-")) {
            displayText = "0";
        }
    }

    private void clearCalculate() {
        result = 0;
        displayText = "0";
        displayHistoryText = "";
        isLastOperation = false;
        operation = "";
    }

    private void setListenerForButtons() {
        GridLayout gridLayout = findViewById(R.id.buttons_grid);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof Button) {
                view.setOnClickListener(this);
            }
        }
    }
}