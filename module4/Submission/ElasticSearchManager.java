package com.example.sharingapp;

/**
 * For remote machine: SERVER = "http://34.202.206.222:8080"
 * -------------------------------------------------------------------------------------------------
 * curl -XDELETE 'http://34.202.206.222:8080/INDEX' - can be used to delete ALL objects on the server
 * (items, users, and bids) at that index
 * view an item at: http://34.202.206.222:8080/INDEX/items/item_id
 * view a user at: http://34.202.206.222:8080/INDEX/users/user_id
 * view a bid at: http://34.202.206.222:8080/INDEX/bids/bid_id
 * Where INDEX is replaced with the random number string you generate as per the assignment
 * instructions. Note: item_ids and user_ids are printed to the log (See the Android Monitor)
 * as each user/item is added.
 */


import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

public class ElasticSearchManager {

    private static final String SERVER = "http://34.202.206.222:8080";
    private static final String INDEX = "488346365"; // ⚠️ CHANGE THIS TO YOUR OWN RANDOM NUMBER
    private static final String ITEM_TYPE = "items";
    private static final String USER_TYPE = "users";
    private static final String BID_TYPE = "bids";   // ✅ Added for bids

    private static JestDroidClient client;

    // ---------------------------------------------------------
    // ITEM TASKS
    // ---------------------------------------------------------

    public static class GetItemListTask extends AsyncTask<Void,Void,ArrayList<Item>> {

        @Override
        protected ArrayList<Item> doInBackground(Void... params) {

            verifyConfig();
            ArrayList<Item> items = new ArrayList<>();
            String search_string = "{\"from\":0,\"size\":10000}";

            Search search = new Search.Builder(search_string)
                    .addIndex(INDEX)
                    .addType(ITEM_TYPE)
                    .build();

            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    items = (ArrayList<Item>) execute.getSourceAsObjectList(Item.class);
                    Log.i("ELASTICSEARCH","Item search successful");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return items;
        }
    }

    public static class AddItemTask extends AsyncTask<Item,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Item... params) {

            verifyConfig();
            Boolean success = false;
            Item item = params[0];

            String id = item.getId();

            Index index = new Index.Builder(item)
                    .index(INDEX)
                    .type(ITEM_TYPE)
                    .id(id)
                    .build();

            try {
                DocumentResult execute = client.execute(index);
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Add item successful");
                    success = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    public static class RemoveItemTask extends AsyncTask<Item,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Item... params) {

            verifyConfig();
            Boolean success = false;
            Item item = params[0];

            try {
                DocumentResult execute = client.execute(
                        new Delete.Builder(item.getId())
                                .index(INDEX)
                                .type(ITEM_TYPE)
                                .build()
                );

                if(execute.isSucceeded()) {
                    success = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    // ---------------------------------------------------------
    // USER TASKS
    // ---------------------------------------------------------

    public static class GetUserListTask extends AsyncTask<Void,Void,ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(Void... params) {

            verifyConfig();
            ArrayList<User> users = new ArrayList<>();
            String search_string = "{\"from\":0,\"size\":10000}";

            Search search = new Search.Builder(search_string)
                    .addIndex(INDEX)
                    .addType(USER_TYPE)
                    .build();

            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<User> remoteUsers = execute.getSourceAsObjectList(User.class);
                    users.addAll(remoteUsers);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return users;
        }
    }

    public static class AddUserTask extends AsyncTask<User,Void,Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {

            verifyConfig();
            Boolean success = false;
            User user = params[0];

            String id = user.getId();

            Index index = new Index.Builder(user)
                    .index(INDEX)
                    .type(USER_TYPE)
                    .id(id)
                    .build();

            try {
                DocumentResult execute = client.execute(index);
                if(execute.isSucceeded()) {
                    success = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    public static class RemoveUserTask extends AsyncTask<User,Void,Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {

            verifyConfig();
            Boolean success = false;
            User user = params[0];

            try {
                DocumentResult execute = client.execute(
                        new Delete.Builder(user.getId())
                                .index(INDEX)
                                .type(USER_TYPE)
                                .build()
                );

                if(execute.isSucceeded()) {
                    success = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    // ---------------------------------------------------------
    // BID TASKS (NEW)
    // ---------------------------------------------------------

    public static class GetBidListTask extends AsyncTask<Void,Void,ArrayList<Bid>> {

        @Override
        protected ArrayList<Bid> doInBackground(Void... params) {

            verifyConfig();
            ArrayList<Bid> bids = new ArrayList<>();
            String search_string = "{\"from\":0,\"size\":10000}";

            Search search = new Search.Builder(search_string)
                    .addIndex(INDEX)
                    .addType(BID_TYPE)
                    .build();

            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<Bid> remoteBids = execute.getSourceAsObjectList(Bid.class);
                    bids.addAll(remoteBids);
                    Log.i("ELASTICSEARCH","Bid search successful");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bids;
        }
    }

    public static class AddBidTask extends AsyncTask<Bid,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Bid... params) {

            verifyConfig();
            Boolean success = false;
            Bid bid = params[0];

            String id = bid.getBidId();

            Index index = new Index.Builder(bid)
                    .index(INDEX)
                    .type(BID_TYPE)
                    .id(id)
                    .build();

            try {
                DocumentResult execute = client.execute(index);
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Add bid successful");
                    success = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    public static class RemoveBidTask extends AsyncTask<Bid,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Bid... params) {

            verifyConfig();
            Boolean success = false;
            Bid bid = params[0];

            try {
                DocumentResult execute = client.execute(
                        new Delete.Builder(bid.getBidId())
                                .index(INDEX)
                                .type(BID_TYPE)
                                .build()
                );

                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Delete bid successful");
                    success = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    // ---------------------------------------------------------
    // CLIENT CONFIG
    // ---------------------------------------------------------

    private static void verifyConfig() {

        if(client == null) {
            DroidClientConfig.Builder builder =
                    new DroidClientConfig.Builder(SERVER);

            DroidClientConfig config = builder.build();
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}