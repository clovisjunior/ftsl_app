package br.org.ftsl.adapter;

import java.util.HashSet;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.org.ftsl.activities.NavigationMain;
import br.org.ftsl.fragments.MyAgendaFragment;
import br.org.ftsl.fragments.MyAgendaViewPagerFragment;
import br.org.ftsl.navigation.R;

public class NavigationAdapter extends ArrayAdapter<NavigationItemAdapter> {

	private ViewHolder mHolder;
	private HashSet<Integer> mCheckedItems;
	public NavigationAdapter(Context context) {
		super(context, 0);
		this.mCheckedItems = new HashSet<Integer>();
	}

	public void addItem(NavigationItemAdapter itemModel) {
		add(itemModel);
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).isHeader ? 0 : 1;
	}

	@Override
	public boolean isEnabled(int position) {
		return !getItem(position).isHeader;
	}

	public void setChecked(int pos, boolean checked){

        if (checked) {
            this.mCheckedItems.add(Integer.valueOf(pos));
        }
        else {
            this.mCheckedItems.remove(Integer.valueOf(pos));
        }    
        
        this.notifyDataSetChanged();        
    }

	public void resetCheck(){
        this.mCheckedItems.clear();
        this.notifyDataSetChanged();
    }	
	
	public static class ViewHolder {

		public final ImageView icon;
        public final TextView counter;
        public final TextView title;
		public final LinearLayout colorLinear;
		public final View viewNavigation;

		public ViewHolder(TextView title, TextView counter, ImageView icon, LinearLayout colorLinear, View viewNavigation) {
			this.title = title;
            this.counter = counter;
			this.icon = icon;
			this.colorLinear = colorLinear;
			this.viewNavigation = viewNavigation;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		mHolder = null;
		View view = convertView;
		NavigationItemAdapter item = getItem(position);
		
		if (view == null) {

			int layout = 0;			
			layout = R.layout.navigation_item_counter;
						
			if (item.isHeader){
				layout = R.layout.navigation_header_title;
			}

			view = LayoutInflater.from(getContext()).inflate(layout, null);

			TextView txtTitle = (TextView) view.findViewById(R.id.title);
            TextView txtCounter = (TextView) view.findViewById(R.id.counter);
			ImageView imgIcon = (ImageView) view.findViewById(R.id.icon);
			View viewNavigation = view.findViewById(R.id.viewNavigation);
			
			LinearLayout linearColor = (LinearLayout) view.findViewById(R.id.ns_menu_row);
			view.setTag(new ViewHolder(txtTitle, txtCounter, imgIcon, linearColor, viewNavigation));
		}
		
		if (mHolder == null && view != null) {
			Object tag = view.getTag();
			if (tag instanceof ViewHolder) {
				mHolder = (ViewHolder) tag;
			}
		}
				
		if (item != null && mHolder != null) {
			if (mHolder.title != null) {
                mHolder.title.setText(item.title);
                mHolder.title.setTextSize(20);
            }

            if (mHolder.counter != null && position == 1) {
                int counter = ((NavigationMain)getContext()).getAgendaSize();
                if (counter > 0) {
                    mHolder.counter.setVisibility(View.VISIBLE);
                    mHolder.counter.setText("" + counter);
                } else {
                    mHolder.counter.setVisibility(View.GONE);
                }
            }

			if (mHolder.icon != null) {
				if (item.icon != 0) {
					mHolder.title.setTextSize(14);
					mHolder.icon.setVisibility(View.VISIBLE);
					mHolder.icon.setImageResource(item.icon);
				} else {
					mHolder.title.setTextSize(22);
					mHolder.icon.setVisibility(View.GONE);
				}
			}
		}
	    
		mHolder.viewNavigation.setVisibility(View.GONE);
		if (!item.isHeader) {
			if (mCheckedItems.contains(Integer.valueOf(position))) {
				mHolder.title.setTypeface(null,Typeface.BOLD);
				mHolder.viewNavigation.setVisibility(View.VISIBLE);
			} else {				
				mHolder.title.setTypeface(null,Typeface.NORMAL);
			}			
		}
		
		view.setBackgroundResource(R.drawable.seletor_item_navigation);		
	    return view;		
	}

}
