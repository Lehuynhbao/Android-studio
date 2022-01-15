package com.example.scanqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private int layout;
    private List<Account> accountList;
    private List<Account> accountListOld;

    public AccountAdapter(Context context, int layout, List<Account> accountList) {
        this.context = context;
        this.layout = layout;
        this.accountList = accountList;
        this.accountListOld=accountList;
    }

    @Override
    public int getCount() {
        return accountList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch=constraint.toString();
                if(strSearch.isEmpty()){
                    accountList=accountListOld;
                } else {
                    List<Account> list=new ArrayList<>();
                    for(Account account:accountListOld){
                        if(account.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(account);
                        }
                    }
                    accountList=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=accountList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                accountList= (List<Account>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    private  class ViewHolder{
        TextView txtname,txtday;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);
            holder.txtname=(TextView) convertView.findViewById(R.id.txt_listHome_hoten);
            holder.txtday=(TextView) convertView.findViewById(R.id.txt_listHome_ngay);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }
        Account account=accountList.get(position);
        holder.txtname.setText(account.getName());
        holder.txtday.setText("Ngày tiếp súc: "+account.getDate());
        return convertView;
    }
}
