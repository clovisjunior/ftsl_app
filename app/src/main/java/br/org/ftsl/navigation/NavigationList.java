package br.org.ftsl.navigation;

import android.content.Context;
import br.org.ftsl.adapter.NavigationAdapter;
import br.org.ftsl.adapter.NavigationItemAdapter;
import br.org.ftsl.database.DatabaseHelper;
import br.org.ftsl.utils.Utils;

public class NavigationList {

	public static NavigationAdapter getNavigationAdapter(Context context){

		NavigationAdapter navigationAdapter = new NavigationAdapter(context);		
		String[] menuItems = context.getResources().getStringArray(R.array.nav_menu_items);
		
		for (int i = 0; i < menuItems.length; i++) {
			
			String title = menuItems[i];				
			NavigationItemAdapter itemNavigation;

            //Agenda
            if(i == 1){
                itemNavigation = new NavigationItemAdapter(title, Utils.iconNavigation[i], false, 0);
            }
            else {
                itemNavigation = new NavigationItemAdapter(title, Utils.iconNavigation[i]);
            }
			navigationAdapter.addItem(itemNavigation);
		}

		return navigationAdapter;
	}	
}
