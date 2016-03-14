package com.gelato.gelato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gelato.gelato.models.User;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mathpresso2 on 2015-09-10.
 */

public class UserListAdapter extends BaseAdapter {

    // 문자열을 보관 할 ArrayList
    private ArrayList<User> m_List;

    // 생성자
    public UserListAdapter() {
        m_List = new ArrayList<User>();
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return m_List.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_user, parent, false);

            // TextView에 현재 position의 문자열 추가
            TextView text = (TextView) convertView.findViewById(R.id.txtvName);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imgvPortrait);

            text.setText(m_List.get(position).getUserName());
            if (!m_List.get(position).getUserProfileUrl().contentEquals("0")) {
                Glide.with(context)
                        .load(m_List.get(position).getUserProfileUrl())
                        .bitmapTransform(new CropCircleTransformation(context))
                        .placeholder(R.drawable.ic_account_circle_black_48dp)
                        .into(imageView);
            }
        }

        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(User _msg) {
        m_List.add(_msg);
        notifyDataSetChanged();
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
        notifyDataSetChanged();
    }
}
