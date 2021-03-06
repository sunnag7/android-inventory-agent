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
 * @author    Ivan Del Pino
 * @copyright Copyright Teclib. All rights reserved.
 * @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 * @link      https://github.com/flyve-mdm/android-inventory-agent
 * @link      https://flyve-mdm.com
 * ------------------------------------------------------------------------------
 */

package org.flyve.inventory.agent.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.flyve.inventory.agent.R;
import org.flyve.inventory.agent.adapter.ListServersAdapter;
import org.flyve.inventory.agent.core.servers.Servers;
import org.flyve.inventory.agent.core.servers.ServersPresenter;
import org.flyve.inventory.agent.utils.FlyveLog;
import org.flyve.inventory.agent.utils.Helpers;

import java.util.ArrayList;

public class ActivityListServers extends AppCompatActivity implements Servers.View {

    private Servers.Presenter presenter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.loadServers(ActivityListServers.this);
        }
    };

    /**
     * Called when the activity is starting, inflates the activity's UI
     * @param savedInstanceState if the activity is re-initialized, it contains the data it most recently supplied
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_servers);
        presenter = new ServersPresenter(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception ex) {
            FlyveLog.e(ex.getMessage());
        }

        Button addServer = toolbar.findViewById(R.id.addServer);
        addServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityDetailServer.class));
            }
        });

        presenter.loadServers(this);

        IntentFilter filter = new IntentFilter("reload-servers");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }


    @Override
    public void showError(String message) {
        Helpers.snackClose(ActivityListServers.this, message, getString(R.string.permission_snack_ok), true);
    }

    @Override
    public void showServer(ArrayList<String> model) {
        RelativeLayout containerNoServer = findViewById(R.id.containerNoServer);
        RecyclerView listServer = findViewById(R.id.recyclerListServer);
        if (model.size() == 0) {
            containerNoServer.setVisibility(View.VISIBLE);
            listServer.setVisibility(View.GONE);
        } else {
            listServer.setVisibility(View.VISIBLE);
            containerNoServer.setVisibility(View.GONE);
            ListServersAdapter adapter = new ListServersAdapter(model, this);
            listServer.setLayoutManager(new LinearLayoutManager(this));
            listServer.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
