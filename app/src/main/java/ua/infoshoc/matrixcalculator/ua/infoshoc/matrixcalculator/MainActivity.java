package ua.infoshoc.matrixcalculator;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait Orientation
            findViewById(R.id.determinant_button).setOnClickListener(this);
            findViewById(R.id.rank_button).setOnClickListener(this);
            findViewById(R.id.transposed_button).setOnClickListener(this);
            findViewById(R.id.rref_button).setOnClickListener(this);
            findViewById(R.id.reverse_button).setOnClickListener(this);
            findViewById(R.id.adjugate_button).setOnClickListener(this);
            findViewById(R.id.power_button).setOnClickListener(this);
        } else {
            // Landscape Orientation
            findViewById(R.id.plus_button).setOnClickListener(this);
            findViewById(R.id.minus_button).setOnClickListener(this);
            findViewById(R.id.multiply_button).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        MatrixOutFragment outputMatrixFragment = (MatrixOutFragment) getFragmentManager().findFragmentById(R.id.output_matrix_fragment);
        TextView answerTextView = (TextView) findViewById(R.id.answer_textView);
        outputMatrixFragment.clearComposingMatrix();
        answerTextView.clearComposingText();


        MatrixInFragment matrixInFragment = (MatrixInFragment) getFragmentManager().findFragmentById(R.id.input_matrix_fragment);
        MatrixInFragment matrix1InFragment = (MatrixInFragment) getFragmentManager().findFragmentById(R.id.input1_matrix_fragment);
        MatrixInFragment matrix2InFragment = (MatrixInFragment) getFragmentManager().findFragmentById(R.id.input2_matrix_fragment);

        Matrix matrixIn = null, matrix1In = null, matrix2In = null;
        if (matrixInFragment != null && matrixInFragment.isVisible()) {
            matrixIn = matrixInFragment.getMatrix();
        }
        if (matrix1InFragment != null && matrix1InFragment.isVisible()) {
            matrix1In = matrix1InFragment.getMatrix();
        }
        if (matrix2InFragment != null && matrix2InFragment.isVisible()) {
            matrix2In = matrix2InFragment.getMatrix();
        }

        String answerText = null;
        Matrix result = null;
        switch (v.getId()) {
            case R.id.determinant_button:
                try {
                    Element answer_element = matrixIn.getDeterminant();
                    answerText = answer_element.getString();
                } catch (Matrix.NotSquareMatrixException e) {
                    answerText = getString(R.string.not_square_matrix_error);
                }
                break;

            case R.id.rank_button:
                Integer rank = matrixIn.getRank();
                answerText = rank.toString();
                break;

            case R.id.transposed_button:
                result = matrixIn.getTransposed();
                break;

            case R.id.rref_button:
                result = matrixIn.getRREF();
                break;

            case R.id.reverse_button:
                try {
                    result = matrixIn.getReverse();
                } catch (Matrix.NotSquareMatrixException e) {
                    answerText = getString(R.string.not_square_matrix_error);
                } catch (Matrix.IrreversibleMatrixException e) {
                    answerText = getString(R.string.irreversible_matrix_error);
                }
                break;

            case R.id.adjugate_button:
                try {
                    result = matrixIn.getAdjugate();
                } catch (Matrix.NotSquareMatrixException e) {
                    answerText = getString(R.string.not_square_matrix_error);
                }
                break;
            case R.id.power_button:
                EditText exponentEditText = (EditText) findViewById(R.id.exponent_edit_text);
                String exponentString = exponentEditText.getText().toString();
                int exponent = Integer.parseInt(exponentString);
                try {
                    result = matrixIn.binpow(exponent);
                } catch (Matrix.NotSquareMatrixException e) {
                    answerText = getString(R.string.not_square_matrix_error);
                }
                break;

            case R.id.plus_button:
                try {
                    result = matrix1In.add(matrix2In);
                } catch (Matrix.DifferentMatricesSizeException e) {
                    answerText = getString(R.string.different_matrices_size_error);
                }
                break;
            case R.id.minus_button:
                try {
                    result = matrix1In.substract(matrix2In);
                } catch (Matrix.DifferentMatricesSizeException e) {
                    answerText = getString(R.string.different_matrices_size_error);
                }
                break;
            case R.id.multiply_button:
                try {
                    result = matrix1In.multiply(matrix2In);
                } catch (Matrix.InappropriateMatricesSizeException e) {
                    answerText = getString(R.string.inappropriate_matrices_size_error);
                }
                break;
        }

        if (result != null) {
            outputMatrixFragment.setMatrix(result);
        }

        if (answerText != null) {
            answerTextView.setText(answerText);
        }
    }
}
