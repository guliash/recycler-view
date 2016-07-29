package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.decorate)
    Switch decorate;

    @BindView(R.id.columns)
    EditText columns;

    private ColorsItemDecoration mStrokeDecoration;

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

        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.setAdapter(colorsAdapter);
        rv.setItemAnimator(new ColorsItemAnimator());
        rv.setItemViewCacheSize(120);
        rv.getRecycledViewPool().setMaxRecycledViews(0, 120);
        mStrokeDecoration = new ColorsItemDecoration(10, Color.GRAY, Color.YELLOW);

        rv.addOnScrollListener(new ColorsScrollListener(rv));

    }

    @OnCheckedChanged(R.id.decorate)
    void onDecorateChecked() {
        handleDecoration(decorate.isChecked());
    }

    private void handleDecoration(boolean show) {
        if(show) {
            rv.addItemDecoration(mStrokeDecoration);
        } else {
            rv.removeItemDecoration(mStrokeDecoration);
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

    private void showNaturalWarning() {
        columns.setError(getString(R.string.natural_warning));
    }

    private void updateColumns(int newCount) {
        GridLayoutManager layoutManager = (GridLayoutManager)rv.getLayoutManager();

        final int oldCount = layoutManager.getSpanCount();
        final int last = layoutManager.findLastVisibleItemPosition();
        final int first = layoutManager.findFirstVisibleItemPosition();
        final int numberAtColumn = (last - first + 1) / oldCount;

        layoutManager.setSpanCount(newCount);

        if(newCount <= oldCount) {
            final int diff = (oldCount - newCount) * numberAtColumn;
            rv.getAdapter().notifyItemRangeInserted(last - diff + 1, diff);
        } else {
            rv.getAdapter().notifyItemRangeInserted(last + 1, (newCount - oldCount) * numberAtColumn);
        }
    }
}
