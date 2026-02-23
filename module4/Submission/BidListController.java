package com.example.sharingapp;

import java.util.ArrayList;

/**
 * BidListController is responsible for all communication between views and BidList model
 */
public class BidListController {

    private BidList bid_list;

    public BidListController(BidList bid_list) {
        this.bid_list = bid_list;
    }

    // ---------------------------------------------------
    // REMOTE METHODS
    // ---------------------------------------------------

    public boolean addBid(Bid bid) {

        AddBidCommand command = new AddBidCommand(bid);
        command.execute();
        return command.isExecuted();
    }

    public boolean removeBid(Bid bid) {
        DeleteBidCommand command = new DeleteBidCommand();
        return command.execute(bid);
    }

    public boolean removeItemBids(String itemId) {

        ArrayList<Bid> allBids = getRemoteBids();

        for (Bid bid : allBids) {
            if (bid.getItemId().equals(itemId)) {

                DeleteBidCommand command = new DeleteBidCommand();
                boolean isExecuted = command.execute(bid);

                if (!isExecuted) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Bid> getRemoteBids() {
        return bid_list.getRemoteBids();
    }

    // ---------------------------------------------------
    // OPTIONAL: If still needed for UI display
    // ---------------------------------------------------

    public ArrayList<Bid> getItemBids(String itemId) {

        ArrayList<Bid> filtered = new ArrayList<>();
        ArrayList<Bid> all = getRemoteBids();

        for (Bid bid : all) {
            if (bid.getItemId().equals(itemId)) {
                filtered.add(bid);
            }
        }
        return filtered;
    }

    public Float getHighestBid(String itemId) {

        Float highest = null;
        for (Bid bid : getItemBids(itemId)) {
            if (highest == null || bid.getBidAmount() > highest) {
                highest = bid.getBidAmount();
            }
        }
        return highest;
    }

    public String getHighestBidder(String itemId) {

        Float highest = 0f;
        String bidder = null;

        for (Bid bid : getItemBids(itemId)) {
            if (bid.getBidAmount() > highest) {
                highest = bid.getBidAmount();
                bidder = bid.getBidderUsername();
            }
        }
        return bidder;
    }
}