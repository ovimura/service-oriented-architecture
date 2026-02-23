package com.example.sharingapp;

/**
 * Command to add a bid
 */
public class AddBidCommand extends Command {

    private Bid bid;

    public AddBidCommand(Bid bid) {
        this.bid = bid;
    }

    @Override
    public void execute() {

        ElasticSearchManager.AddBidTask task =
                new ElasticSearchManager.AddBidTask();

        try {
            Boolean success = task.execute(bid).get();
            super.setIsExecuted(success);
        } catch (Exception e) {
            e.printStackTrace();
            super.setIsExecuted(false);
        }
    }
}
