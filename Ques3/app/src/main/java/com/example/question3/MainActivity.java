package com.example.question3;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.RenderMode;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    Spinner inputUnitSpinner, outputUnitSpinner;
    TextView resultText;
    Button convertButton;
    ImageButton swapButton;
    LottieAnimationView animationView;

    String[] inputUnits = {"Select source unit", "Feet", "Inches", "Centimeters", "Meters", "Yards", "Miles", "Kilometers"};
    String[] outputUnits = {"Select target unit", "Feet", "Inches", "Centimeters", "Meters", "Yards", "Miles", "Kilometers"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        inputValue = findViewById(R.id.inputValue);
        inputUnitSpinner = findViewById(R.id.inputUnitSpinner);
        outputUnitSpinner = findViewById(R.id.outputUnitSpinner);
        resultText = findViewById(R.id.resultText);
        convertButton = findViewById(R.id.convertButton);
        swapButton = findViewById(R.id.swapButton);
        animationView = findViewById(R.id.lottieAnimationView);

        // Configure the Lottie Animation
        setupAnimation();

        // Setup adapters with custom styling
        ArrayAdapter<String> inputAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, inputUnits) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable first item
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }
        };

        ArrayAdapter<String> outputAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, outputUnits) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }
        };

        inputAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        outputAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        inputUnitSpinner.setAdapter(inputAdapter);
        outputUnitSpinner.setAdapter(outputAdapter);

        // Convert button logic
        convertButton.setOnClickListener(v -> {
            convert();
            // Play animation when converting
            if (!animationView.isAnimating()) {
                animationView.playAnimation();
            }
        });

        // Swap button logic
        swapButton.setOnClickListener(v -> swapUnits());
    }

    private void setupAnimation() {
        // The animation resource is set in XML, but we can adjust properties here
        animationView.setAnimation(R.raw.newanimation);
        animationView.setRepeatCount(LottieDrawable.INFINITE);
        animationView.setSpeed(1.0f);

        // You can also enable hardware acceleration for smoother animations
        animationView.setRenderMode(RenderMode.HARDWARE);

        // Auto-play the animation
        animationView.playAnimation();
    }

    private void swapUnits() {
        int fromPosition = inputUnitSpinner.getSelectedItemPosition();
        int toPosition = outputUnitSpinner.getSelectedItemPosition();

        // Don't swap if either is the default "Select" position
        if (fromPosition == 0 || toPosition == 0) {
            Toast.makeText(this, "Please select valid units to swap", Toast.LENGTH_SHORT).show();
            return;
        }

        inputUnitSpinner.setSelection(toPosition);
        outputUnitSpinner.setSelection(fromPosition);

        // Auto-convert after swapping
        if (!inputValue.getText().toString().trim().isEmpty()) {
            convert();
        }
    }

    void convert() {
        String inputStr = inputValue.getText().toString().trim();
        if (inputStr.isEmpty()) {
            resultText.setText("Please enter a value");
            return;
        }

        String fromUnit = inputUnitSpinner.getSelectedItem().toString();
        String toUnit = outputUnitSpinner.getSelectedItem().toString();

        // Check if user selected valid units
        if (fromUnit.startsWith("Select") || toUnit.startsWith("Select")) {
            resultText.setText("Please select valid units");
            return;
        }

        try {
            double input = Double.parseDouble(inputStr);
            double meters = toMeters(input, fromUnit);
            double result = fromMeters(meters, toUnit);

            resultText.setText(String.format("%.4f %s", result, toUnit));
        } catch (NumberFormatException e) {
            resultText.setText("Invalid number format");
        }
    }

    double toMeters(double value, String unit) {
        switch (unit) {
            case "Feet": return value * 0.3048;
            case "Inches": return value * 0.0254;
            case "Centimeters": return value / 100.0;
            case "Meters": return value;
            case "Yards": return value * 0.9144;
            case "Miles": return value * 1609.34;
            case "Kilometers": return value * 1000.0;
            default: return 0;
        }
    }

    double fromMeters(double meters, String unit) {
        switch (unit) {
            case "Feet": return meters / 0.3048;
            case "Inches": return meters / 0.0254;
            case "Centimeters": return meters * 100;
            case "Meters": return meters;
            case "Yards": return meters / 0.9144;
            case "Miles": return meters / 1609.34;
            case "Kilometers": return meters / 1000.0;
            default: return 0;
        }
    }
}