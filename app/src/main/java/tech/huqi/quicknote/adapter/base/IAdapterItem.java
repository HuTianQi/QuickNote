package tech.huqi.quicknote.adapter.base;

/**
 * Created by hzhuqi on 2019/4/10
 */
public interface IAdapterItem<T> {
    /**
     * 将数据data绑定到position位置的View上
     *
     * @param data
     * @param position
     */
    void bindDataToView(T data, int position);
}
