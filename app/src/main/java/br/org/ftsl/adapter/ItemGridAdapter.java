package br.org.ftsl.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import br.org.ftsl.model.AuthorModel;
import br.org.ftsl.model.ItemGridModel;
import br.org.ftsl.navigation.R;
import br.org.ftsl.utils.Utils;

/**
 * Created by 05081364908 on 14/07/14.
 */
public class ItemGridAdapter extends BaseAdapter implements Filterable{

    private final Context mContext;
    private List<ItemGridModel> mItemsAll;
    private List<ItemGridModel> mItems;
    private Boolean mIsAgenda;
    private Pattern mPattern;
    private Integer mItemsCount;

    static class ViewHolder {

        TextView txtSchedule;
        TextView txtTitle;
        TextView txtAuthor;
        ImageView imgCalendar;

    }

    public ItemGridAdapter(Context context, List<ItemGridModel> items, Boolean isAgenda){
        this.mContext = context;
        this.mItemsAll = items;
        this.mIsAgenda = isAgenda;
        this.mPattern = Pattern.compile("");
        this.mItemsCount = items.size();
        this.mItems = new ArrayList<ItemGridModel>(items);
    }

    public List<ItemGridModel> getItemsAll(){
        return this.mItems;
    }

    @Override
    public int getCount() {
        return this.mItemsCount;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((ItemGridModel)getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid, null);

            ViewHolder holder = new ViewHolder();
            holder.txtSchedule = (TextView) convertView.findViewById(R.id.grid_item_schedule);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.grid_item_title);
            holder.txtAuthor = (TextView) convertView.findViewById(R.id.grid_item_author);
            holder.imgCalendar = (ImageView) convertView.findViewById(R.id.grid_item_calendar);

            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        ItemGridModel item = mItems.get(position);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        holder.txtTitle.setText(Html.fromHtml(item.getTitle()).toString());
        holder.txtSchedule.setText(format.format(item.getInicio()) + " - " + format.format(item.getFim()) + " [" + mContext.getResources().getStringArray(R.array.event_types_items)[item.getType()] + "]");

        String authors;
        AuthorModel author = item.getAuthor();

        authors = author.getName();

        if("admin".equals(authors)){
            authors = "Organização";
        }

        holder.txtAuthor.setText(authors);

        holder.imgCalendar.setVisibility(View.INVISIBLE);
        if(item.getAssistir() && mIsAgenda == Boolean.FALSE){
            holder.imgCalendar.setVisibility(View.VISIBLE);
        }

        convertView.setBackgroundColor(convertView.getResources().getColor(R.color.transparent));
        if(item.getFim().before(new Date(System.currentTimeMillis()))){
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.event_past));
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                try {
                    FilterResults results = new FilterResults();
                    if (constraint == null || constraint.equals("")) {
                        mPattern = Pattern.compile("");
                        results.values = mItemsAll;
                        results.count = mItemsAll.size();
                        return results;
                    }
                    String c = constraint.toString().toLowerCase();
                    ArrayList<ItemGridModel> values = new ArrayList<ItemGridModel>();
                    ArrayList<ItemGridModel> temp1 = new ArrayList<ItemGridModel>();
                    ArrayList<ItemGridModel> temp2 = new ArrayList<ItemGridModel>();

                    if (c.contains("|")) {
                        String[] matches = c.split("\\|");
                        for (ItemGridModel item : mItemsAll) {
                            String name = item.getTitle().toLowerCase();
                            for (String match : matches) {
                                if (name.contains(match)) {
                                    values.add(item);
                                    break;
                                }
                            }
                        }
                    } else {
                        //Filter by Titulo
                        for (ItemGridModel item : mItemsAll) {
                            String name = Utils.removeAccentuation(item.getTitle());
                            if (name.matches(c + ".*")) {
                                values.add(item);
                            } else {
                                if (name.matches(".* " + c + ".*")) {
                                    temp1.add(item);
                                } else {
                                    if (name.matches("[^ ]+" + c + ".*")) {
                                        temp2.add(item);
                                    }
                                }
                            }
                        }

                        //Filter by Autor
                        for (ItemGridModel item : mItemsAll) {
                            AuthorModel author = item.getAuthor();
                            String name = Utils.removeAccentuation(author.getName());
                            if (name.matches(c + ".*")) {
                                values.add(item);
                            } else {
                                if (name.matches(".* " + c + ".*")) {
                                    temp1.add(item);
                                } else {
                                    if (name.matches("[^ ]+" + c + ".*")) {
                                        temp2.add(item);
                                    }
                                }
                            }
                        }
                    }
                    values.addAll(temp1);
                    values.addAll(temp2);
                    results.values = values;
                    results.count = values.size();

                    return results;
                }
                catch(PatternSyntaxException e){

                }
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                try {
                    mPattern = Pattern.compile(constraint.toString().toLowerCase());
                    mItemsCount = results.count;
                    mItems = (ArrayList<ItemGridModel>) results.values;

                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
                catch(PatternSyntaxException e){

                }
            }
        };
    }


}
