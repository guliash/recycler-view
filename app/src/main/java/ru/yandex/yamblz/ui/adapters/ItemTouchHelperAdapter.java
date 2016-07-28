package ru.yandex.yamblz.ui.adapters;

public interface ItemTouchHelperAdapter {

    void onItemMove(int from, int to);

    void onItemDismiss(int position);

    void onItemsFinalMove(int from, int to);

    void onItemsStartMove();
}
