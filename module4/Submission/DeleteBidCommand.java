package com.example.sharingapp;

/**
 * Command to delete a bid
 */

public class DeleteBidCommand {

    public boolean execute(Bid bid) {

        ElasticSearchManager.RemoveBidTask task =
                new ElasticSearchManager.RemoveBidTask();

        try {
            return task.execute(bid).get();   // wait for result
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
