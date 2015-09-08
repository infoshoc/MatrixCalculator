package ua.infoshoc.matrixcalculator;


import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatrixInFragment extends Fragment {

    public MatrixInFragment() {
        // Required empty public constructor
    }

    public Matrix getMatrix() {
        int height = 0, width = 0;
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.table_layout);
        for (int row = 0; row < tableLayout.getChildCount(); ++row) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(row);
            for (int col = 0; col < tableRow.getChildCount(); ++col) {
                EditText editText = (EditText) tableRow.getChildAt(col);
                if (editText.getText().length() > 0) {
                    width = width < col + 1 ? col + 1 : width;
                    height = row + 1;
                }
            }
        }
        ua.infoshoc.matrixcalculator.Matrix result = new ua.infoshoc.matrixcalculator.Matrix(height, width);
        for (int row = 0; row < height; ++row) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(row);
            for (int col = 0; col < width; ++col) {
                EditText editText = (EditText) tableRow.getChildAt(col);
                result.set(row, col, new ua.infoshoc.matrixcalculator.Element(editText.getText().toString()));
            }
        }
        return result;
    }

    public void setMatrix(Matrix matrix, Context context) {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.table_layout);
        tableLayout.removeAllViews();
        for (int row = 0; row < matrix.getHeight(); ++row) {
            TableRow tableRow = new TableRow(context);
            for (int col = 0; col < matrix.getWidth(); ++col) {
                EditText editText = getField(context);
                editText.setText(matrix.get(row, col).getString());
                tableRow.addView(editText);
            }
            tableLayout.addView(tableRow);
        }
    }

    private int rows, cols;

    private EditText getField(Context context) {
        EditText editText = new EditText(context);
        editText.setWidth(40);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setId(View.generateViewId());
        return editText;
    }

    private EditText get(int row, int col) {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.table_layout);
        TableRow tableRow = (TableRow) tableLayout.getChildAt(row);
        EditText editText = (EditText) tableRow.getChildAt(col);
        return editText;
    }

    private Button removeColumnButton,removeRowButton;

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.table_layout);
            switch(v.getId()){
                case R.id.add_col:
                    ++cols;
                    for (int row = 0; row < tableLayout.getChildCount(); ++row){
                        TableRow tableRow = (TableRow) tableLayout.getChildAt(row);
                        EditText editText = getField(getView().getContext());
                        tableRow.addView(editText);
                    }
                    break;
                case R.id.rem_col:
                    --cols;
                    for (int row = 0; row < tableLayout.getChildCount(); ++row){
                        TableRow tableRow = (TableRow) tableLayout.getChildAt(row);
                        tableRow.removeViewAt(tableRow.getChildCount()-1);
                    }
                    break;
                case R.id.add_row:
                    ++rows;
                    TableRow tableRow = new TableRow(getView().getContext());
                    for (int col = 0; col < cols; ++col) {
                        EditText editText = getField(getView().getContext());
                        tableRow.addView(editText);
                    }
                    tableLayout.addView(tableRow);
                    break;
                case R.id.rem_row:
                    --rows;
                    tableLayout.removeViewAt(tableLayout.getChildCount() - 1);
                    break;
            }


            removeColumnButton.setEnabled(cols > 1);
            removeRowButton.setEnabled(rows > 1);

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    int next_col = (col + 1)%cols;
                    int next_row = row + (next_col == 0 ? 1 : 0);
                    if (next_row != rows) {
                        if (Debug.enabled) {
                            Log.d("DEBUG", "next(" + row + "," + col + ")[id=" + get(row, col).getId() + "]=(" + next_row + "," + next_col + ")[id=" + get(next_row, next_col).getId() + "]");
                        }
                        get(row, col).setNextFocusDownId(get(next_row, next_col).getId());
                    }
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_matrix_in, container, false);
        removeColumnButton = (Button) result.findViewById(R.id.rem_col);
        removeRowButton = (Button) result.findViewById(R.id.rem_row);
        result.findViewById(R.id.add_row).setOnClickListener(onClick);
        result.findViewById(R.id.add_col).setOnClickListener(onClick);
        removeColumnButton.setOnClickListener(onClick);
        removeRowButton.setOnClickListener(onClick);
        rows = cols = 0;

        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            getView().findViewById(R.id.add_row).callOnClick();
            getView().findViewById(R.id.add_col).callOnClick();
        }
    }
}
