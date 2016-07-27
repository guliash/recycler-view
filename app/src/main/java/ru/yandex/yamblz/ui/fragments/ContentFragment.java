package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.adapters.ContentAdapter;
import ru.yandex.yamblz.ui.adapters.StrokeItemDecoration;
import ru.yandex.yamblz.ui.adapters.ItemTouchHelperCallback;

public class ContentFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.decorate)
    Switch decorate;

    @BindView(R.id.columns)
    EditText columns;

    private StrokeItemDecoration mStrokeDecoration;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ContentAdapter contentAdapter = new ContentAdapter();

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(contentAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);

        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.setAdapter(contentAdapter);
        mStrokeDecoration = new StrokeItemDecoration(10, Color.GRAY);

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
                ((GridLayoutManager) rv.getLayoutManager()).setSpanCount(cols);
                rv.requestLayout();
            }
        } catch (NumberFormatException e) {
            showNaturalWarning();
        }
    }

    private void showNaturalWarning() {
        columns.setError(getString(R.string.natural_warning));
    }
}
