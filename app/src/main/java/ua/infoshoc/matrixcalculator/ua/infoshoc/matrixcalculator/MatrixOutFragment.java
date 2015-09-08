package ua.infoshoc.matrixcalculator;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatrixOutFragment extends Fragment {


    public MatrixOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrix_out, container, false);
    }

    public void setMatrix(Matrix matrix) {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.table_layout);
        tableLayout.removeAllViews();
        for (int row = 0; row < matrix.getHeight(); ++row) {
            TableRow tableRow = new TableRow(getActivity().getApplicationContext());
            for (int col = 0; col < matrix.getWidth(); ++col) {
                TextView textView = new TextView(getActivity().getApplicationContext());
                textView.setText(matrix.get(row, col).getString());
                textView.setPadding(0,0, 10, 0);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
    }

    public void clearComposingMatrix() {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.table_layout);
        tableLayout.removeAllViews();
    }
}
