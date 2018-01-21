package idv.allen.gameball.fragment;

/**
 * Created by Java on 2017/12/24.
 */

public interface ItemTouchHelperAdapter {
    // 資料移動
    boolean onItemMove(int fromPosition, int toPosition);

    // 資料刪除
    void onItemDismiss(int position);
}
