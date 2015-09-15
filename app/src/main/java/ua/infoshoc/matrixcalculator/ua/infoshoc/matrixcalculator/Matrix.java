package ua.infoshoc.matrixcalculator;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Infoshoc_2 on 16.05.2015.
 */
public class Matrix implements Serializable {
    // Errors
    public final class IrreversibleMatrixException extends Exception { }

    public final class NotSquareMatrixException extends Exception { }

    public class InappropriateMatricesSizeException extends Exception { }

    public final class DifferentMatricesSizeException extends InappropriateMatricesSizeException {}

    // Static "constructors"
    public final static Matrix getIdentityMatrix(int size) {
        Matrix result = new Matrix(size, size);
        for (int row = 0; row < size; ++row) {
            result.set(row, row, Element.ONE);
        }
        return result;
    }

    // Fields
    private ArrayList<ArrayList<Element>> items;

    // Constructors
    public Matrix(int height, int width) {
        items = new ArrayList<ArrayList<Element>>();
        for (int row = 0; row < height; ++row) {
            ArrayList<Element> row_elements = new ArrayList<Element>();
            for (int col = 0; col < width; ++col) {
                row_elements.add(Element.ZERO);
            }
            items.add(row_elements);
        }
    }

    // Gets
    private Matrix getMinor(int row, int col) {
        Matrix answer = new Matrix(getHeight() - 1, getWidth() - 1);
        for (int i = 0; i < getHeight(); ++i) {
            if (i == row) {
                continue;
            }
            for (int j = 0; j < getWidth(); ++j) {
                if (j == col) {
                    continue;
                }
                int ii = i > row ? i - 1 : i;
                int jj = j > col ? j - 1 : j;
                answer.set(ii, jj, get(i, j));
            }
        }
        return answer;
    }

    public int getHeight() {
        return items.size();
    }

    public int getWidth() {
        return items.size() == 0 ? 0 : items.get(0).size();
    }

    public Element get(int row, int col) {
        return items.get(row).get(col);
    }

    public Element getDeterminant() throws NotSquareMatrixException {
        if (getWidth() != getHeight()) {
            throw new NotSquareMatrixException();
        }
        if (getWidth() == 1) {
            return get(0, 0);
        }
        int row = 0;
        Element answer = Element.ZERO;
        Integer multiplier = row % 2 == 1 ? -1 : 1;
        for (int col = 0; col < getWidth(); ++col) {
            Matrix minor = getMinor(row, col);
            answer = answer.add(get(row, col).multiply(minor.getDeterminant()).multiply(new Element(multiplier)));
            multiplier *= -1;
        }
        return answer;
    }

    public Matrix getRREF() {
        Matrix result = this;

        int pivotRow = 0;
        for (int pivotCol = 0; pivotCol < getWidth() && pivotRow < getHeight(); ++pivotCol, ++pivotRow) {
            int row = pivotRow;
            if (Debug.enabled) {
                Log.d("DEBUG", "getRREF row=" + row + " pivotCol=" + pivotCol);
            }
            while (row != getHeight() && result.get(row, pivotCol).abs().compareTo(Element.EPS) == -1) { //result.get(row, pivotCol) === 0
                ++row;
            }
            if (row != getHeight()) {
                result.swapRows(row, pivotRow);
                result.multiplyRow(pivotRow, (new Element(1.0)).divide(get(pivotRow, pivotCol)));
                for (row = 0; row != getHeight(); ++row) {
                    if (row != pivotRow) {
                        result.multiplyAndAdd(row, pivotRow, get(row, pivotCol).multiply(Element.MINUS_ONE));
                    }
                }
            }
        }

        return result;
    }

    public Matrix getReverse() throws IrreversibleMatrixException, NotSquareMatrixException {
        if (getWidth() != getHeight()) {
            throw new NotSquareMatrixException();
        }

        Matrix rowReductionMatrix = new Matrix(getHeight(), getWidth() * 2);
        for (int row = 0; row < rowReductionMatrix.getHeight(); ++row) {
            for (int col = 0; col < rowReductionMatrix.getWidth(); ++col) {
                if (col < getWidth()) {
                    rowReductionMatrix.set(row, col, get(row, col));
                } else if (col - getWidth() == row) {
                    rowReductionMatrix.set(row, col, Element.ONE);
                } else {
                    rowReductionMatrix.set(row, col, Element.ZERO);
                }
            }
        }

        Matrix RREFMatrix = rowReductionMatrix.getRREF();

        Matrix result = new Matrix(getHeight(), getWidth());
        for (int row = 0; row < getHeight(); ++row) {
            if (RREFMatrix.get(row, row).substract(Element.ONE).abs().compareTo(Element.EPS) != -1) {
                throw new IrreversibleMatrixException();
            }
            for (int col = 0; col < getWidth(); ++col) {
                if (Debug.enabled) {
                    Log.d("DEBUG", "getReverse row=" + row + " col=" + col);
                }
                result.set(row, col, RREFMatrix.get(row, col + getWidth()));
            }
        }
        return result;
    }

    public Matrix getAdjugate() throws NotSquareMatrixException {
        if (getWidth() != getHeight()) {
            throw new NotSquareMatrixException();
        }
        Matrix result = new Matrix(getHeight(), getWidth());
        for (int row = 0; row < getWidth(); ++row) {
            for (int col = 0; col < getHeight(); ++col) {
                try {
                    result.set(row, col, ((row + col) % 2 == 0 ? Element.ONE : Element.MINUS_ONE).multiply(getMinor(col, row).getDeterminant()));
                } catch (NotSquareMatrixException e) {
                    // Impossible error
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public Matrix getTransposed() {
        Matrix result = new Matrix(getWidth(), getHeight());
        for (int row = 0; row < getWidth(); ++row) {
            for (int col = 0; col < getHeight(); ++col) {
                result.set(row, col, get(col, row));
            }
        }
        return result;
    }

    public int getRank() {
        Matrix rrefMatrix = getRREF();
        int rank, col = -1;
        for (rank = 0; rank < rrefMatrix.getHeight(); ++rank) {
            for (++col; col < getWidth() && rrefMatrix.get(rank, col).compareTo(Element.EPS) == -1; ++col)
                ;
            if (col == getWidth()) {
                break;
            }
        }
        return rank;
    }

    // Sets
    public Matrix set(int row, int col, Element value) {
        items.get(row).set(col, value);
        return this;
    }

    // Methods
    private void swapRows(int row1, int row2) {
        Collections.swap(items, row1, row2);
    }

    private void multiplyRow(int row, Element multiplier) {
        for (int col = 0; col != getWidth(); ++col) {
            set(row, col, get(row, col).multiply(multiplier));
        }
    }

    private void multiplyAndAdd(int target, int pivot, Element multiplier) {
        for (int col = 0; col != getWidth(); ++col) {
            set(target, col, get(target, col).add(get(pivot, col).multiply(multiplier)));
        }
    }

    public Matrix add(Matrix summand) throws DifferentMatricesSizeException {
        if (getWidth() != summand.getWidth() || getHeight() != summand.getHeight())
        {
            throw new DifferentMatricesSizeException();
        }

        Matrix result = new Matrix(getWidth(), getHeight());

        for (int row = 0; row < getHeight(); ++row)
        {
            for (int col = 0; col < getWidth(); ++col)
            {
                result.set(row, col, get(row, col).add(summand.get(row, col)));
            }
        }

        return result;
    }

    public Matrix substract(Matrix subtrahend) throws DifferentMatricesSizeException {
        if (getWidth() != subtrahend.getWidth() || getHeight() != subtrahend.getHeight())
        {
            throw new DifferentMatricesSizeException();
        }

        Matrix result = new Matrix(getWidth(), getHeight());

        for (int row = 0; row < getHeight(); ++row)
        {
            for (int col = 0; col < getWidth(); ++col)
            {
                result.set(row, col, get(row, col).substract(subtrahend.get(row, col)));
            }
        }

        return result;
    }

    public Matrix multiply(Matrix multiplicand) throws InappropriateMatricesSizeException {
        if (getWidth() != multiplicand.getHeight() || getHeight() != multiplicand.getWidth())
        {
            throw new InappropriateMatricesSizeException();
        }

        Matrix result = new Matrix(getHeight(), getHeight());

        for (int row = 0; row < getHeight(); ++row)
        {
            for (int col = 0; col < getHeight(); ++col)
            {
                Element value = Element.ZERO;
                for (int k = 0; k < getWidth(); ++k)
                {
                    value = value.add(get(row, k).multiply(multiplicand.get(k, col)));
                }
                result.set(row, col, value);
            }
        }

        return result;
    }

    public Matrix binpow(int exponent) throws NotSquareMatrixException {
        if (getWidth() != getHeight()) {
            throw new NotSquareMatrixException();
        }

        Matrix result = Matrix.getIdentityMatrix(getWidth());

        Matrix base = this;
        try {
            while (exponent != 0) {
                if (exponent % 2 == 1) {
                    result = result.multiply(base);
                }
                base = base.multiply(base);
                exponent /= 2;
            }
        } catch (InappropriateMatricesSizeException e) {
            // Impossible exception
        }

        return result;
    }
}
