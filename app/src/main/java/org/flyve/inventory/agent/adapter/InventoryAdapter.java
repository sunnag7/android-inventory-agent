/*
 * Copyright Teclib. All rights reserved.
 *
 * Flyve MDM is a mobile device management software.
 *
 * Flyve MDM is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * Flyve MDM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * ------------------------------------------------------------------------------
 * @author    Rafael Hernandez
 * @copyright Copyright Teclib. All rights reserved.
 * @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 * @link      https://github.com/flyve-mdm/android-inventory-agent
 * @link      https://flyve-mdm.com
 * ------------------------------------------------------------------------------
 */

package org.flyve.inventory.agent.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.flyve.inventory.agent.R;
import org.flyve.inventory.agent.schema.ListInventorySchema;

import java.util.ArrayList;


public class InventoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ArrayList<ListInventorySchema>> data;
    private FragmentActivity activity;

    public InventoryAdapter(ArrayList<ArrayList<ListInventorySchema>> data, FragmentActivity fragmentActivity) {
        this.data = data;
        activity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int resource = R.layout.list_item_inventory_parent;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new DataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DataViewHolder) holder).bindData(data.get(position), position);
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        View viewSeparator;

        DataViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            viewSeparator = itemView.findViewById(R.id.viewSeparator);
        }

        void bindData(ArrayList<ListInventorySchema> model, int position) {
            if ((data.size() - 1) == position) {
                viewSeparator.setVisibility(View.GONE);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(new InventoryChildAdapter(model, activity));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}