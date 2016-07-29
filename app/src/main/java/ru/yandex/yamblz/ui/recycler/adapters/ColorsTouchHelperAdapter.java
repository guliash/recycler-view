package ru.yandex.yamblz.ui.recycler.adapters;

/**
 * Helper adapter interface for drag and drop and swipe
 */
public interface ColorsTouchHelperAdapter {

    /**
     * Called when items are swapped
     * @param from the source adapter position
     * @param to the target adapter position
     */
    void onItemMove(int from, int to);

    /**
     * Called when an item is dismissed
     * @param position the adapter position of the item
     */
    void onItemDismiss(int position);
}
