package ua.infoshoc.matrixcalculator;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait Orientation
            findViewById(R.id.determinant_button).setOnClickListener(portraitOnClickListener);
            findViewById(R.id.rank_button).setOnClickListener(portraitOnClickListener);
            findViewById(R.id.transposed_button).setOnClickListener(portraitOnClickListener);
            findViewById(R.id.rref_button).setOnClickListener(portraitOnClickListener);
            findViewById(R.id.reverse_button).setOnClickListener(portraitOnClickListener);
            findViewById(R.id.adjugate_button).setOnClickListener(portraitOnClickListener);
        } else {
            // Landscape Orientation
            findViewById(R.id.plus_button).setOnClickListener(landscapeOnClickListener);
            findViewById(R.id.minus_button).setOnClickListener(landscapeOnClickListener);
            findViewById(R.id.multiply_button).setOnClickListener(landscapeOnClickListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener portraitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MatrixOutFragment outputMatrixFragment = (MatrixOutFragment) getFragmentManager().findFragmentById(R.id.output_matrix_fragment);
            TextView answerTextView = (TextView) findViewById(R.id.answer_textView);
            outputMatrixFragment.clearComposingMatrix();
            answerTextView.clearComposingText();


            MatrixInFragment matrixInFragment = (MatrixInFragment) getFragmentManager().findFragmentById(R.id.input_matrix_fragment);
            Matrix matrixIn = matrixInFragment.getMatrix();

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
            }

            if (result != null) {
                outputMatrixFragment.setMatrix(result);
            }

            if (answerText != null) {
                answerTextView.setText(answerText);
            }

        }
    };

    View.OnClickListener landscapeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MatrixOutFragment outputMatrixFragment = (MatrixOutFragment) getFragmentManager().findFragmentById(R.id.output_matrix_fragment);
            outputMatrixFragment.clearComposingMatrix();

            MatrixInFragment matrix1InFragment = (MatrixInFragment) getFragmentManager().findFragmentById(R.id.input1_matrix_fragment);
            MatrixInFragment matrix2InFragment = (MatrixInFragment) getFragmentManager().findFragmentById(R.id.input2_matrix_fragment);
            TextView answerTextView = (TextView) findViewById(R.id.answer_textView);

            Matrix matrix1In = matrix1InFragment.getMatrix();
            Matrix matrix2In = matrix2InFragment.getMatrix();

            Matrix result = null;
            String answerText = null;
            switch (v.getId()) {
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
    };
}