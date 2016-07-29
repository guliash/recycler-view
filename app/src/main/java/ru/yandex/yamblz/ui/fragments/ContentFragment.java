package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.recycler.adapters.ColorsAdapter;
import ru.yandex.yamblz.ui.recycler.animators.ColorsItemAnimator;
import ru.yandex.yamblz.ui.recycler.helpers.ColorsScrollListener;
import ru.yandex.yamblz.ui.recycler.helpers.ColorsTouchHelperCallback;
import ru.yandex.yamblz.ui.recycler.decorators.ColorsItemDecoration;

public class ContentFragment extends BaseFragment {

    private static final int DEFAULT_COLUMNS_COUNT = 3;
    private static final String COLUMNS_EXTRA = "cols";

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.decorate)
    Switch decorate;

    @BindView(R.id.rotate)
    Switch rotate;

    @BindView(R.id.columns)
    EditText columns;

    private ColorsItemDecoration mStrokeDecoration;
    private ColorsScrollListener mScrollListener;

    private int mCountOfColumns = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            mCountOfColumns = savedInstanceState.getInt(COLUMNS_EXTRA);
        } else {
            mCountOfColumns = DEFAULT_COLUMNS_COUNT;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COLUMNS_EXTRA, mCountOfColumns);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ColorsAdapter colorsAdapter = new ColorsAdapter();

        ItemTouchHelper.Callback callback = new ColorsTouchHelperCallback(colorsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);

        rv.setLayoutManager(new GridLayoutManager(getContext(), mCountOfColumns));
        rv.setAdapter(colorsAdapter);
        rv.setItemAnimator(new ColorsItemAnimator());

        //optimizations for big rows
        rv.setItemViewCacheSize(120);
        rv.setHasFixedSize(true);

        mStrokeDecoration = new ColorsItemDecoration(10, Color.GRAY, Color.YELLOW);
        mScrollListener = new ColorsScrollListener(rv);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        columns.setText(Integer.toString(mCountOfColumns));
        columns.setSelection(columns.getText().length());
    }

    @OnCheckedChanged(R.id.decorate)
    void onDecorateChecked() {
        handleDecoration(decorate.isChecked());
    }

    @OnCheckedChanged(R.id.rotate)
    void onRotateChecked() {
        handleRotation(rotate.isChecked());
    }

    /**
     * turns on/off decoration
     * @param show {@code true} if show
     */
    private void handleDecoration(boolean show) {
        if(show) {
            rv.addItemDecoration(mStrokeDecoration);
        } else {
            rv.removeItemDecoration(mStrokeDecoration);
        }
    }

    /**
     * Turns on/off rotation animation
     * @param rotate {@code true} if turn on
     */
    private void handleRotation(boolean rotate) {
        if(rotate) {
            rv.addOnScrollListener(mScrollListener);
        } else {
            rv.removeOnScrollListener(mScrollListener);
        }
    }

    //TODO save number of columns
    @OnClick(R.id.save_columns)
    void onColumnsSaveClick() {
        String text = columns.getText().toString();
        try {
            int cols = Integer.parseInt(text);
            if(cols <= 0) {
                showNaturalWarning();
            } else {
                updateColumns(cols);
            }
        } catch (NumberFormatException e) {
            showNaturalWarning();
        }
    }

    /**
     * Shows warning that the number of columns should be natural
     */
    private void showNaturalWarning() {
        Snackbar.make(columns, R.string.natural_warning, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Changes number of columns (animating)
     * @param newCount the new count
     */
    private void updateColumns(int newCount) {
        GridLayoutManager layoutManager = (GridLayoutManager)rv.getLayoutManager();
        final int first = layoutManager.findFirstVisibleItemPosition();

        //TODO WTF???? HOW DOES IT WORK?
        layoutManager.setSpanCount(newCount);
        rv.requestLayout();
        rv.getAdapter().notifyItemRangeChanged(first, 0);
    }
}
