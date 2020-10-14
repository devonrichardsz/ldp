import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

final class Requirement {
    public final int numberOfCards;
    public final int numberOfLands;
    public final int cmc;
    public final int[] colorRequirements;

    public Requirement(int numberOfCards, int numberOfLands, int cmc, int[] colorRequirements) {
        this.numberOfCards = numberOfCards;
        this.numberOfLands = numberOfLands;
        this.cmc = cmc;
        this.colorRequirements = colorRequirements;
    }
}

class Deck {
    protected int mNumberOfLands;
    protected int[] mNumberOfGoodLands;
    protected int mNumberOfCards;

    public final void resetDeck (int nrLands, int nrGoodLands, int nrCards) {
        mNumberOfLands=nrLands;
        mNumberOfGoodLands = new int[] { nrGoodLands };
        mNumberOfCards=nrCards;
    }

    public final void resetDeck (int nrLands, int[] nrGoodLands, int nrCards) {
        mNumberOfLands = nrLands;
        mNumberOfGoodLands = nrGoodLands;
        mNumberOfCards = nrCards;
    }

    public final int DrawCard (){
        Random generator = new Random();
        int randIndex = generator.nextInt( this.mNumberOfCards)+1;
        int goodLandCutoff = 0;
        for (int i=0; i < mNumberOfGoodLands.length; i++) {
            goodLandCutoff += mNumberOfGoodLands[i];
            if (randIndex <= goodLandCutoff) {
                mNumberOfGoodLands[i]--;
                mNumberOfLands--;
                mNumberOfCards--;
                return i + 1;
            }
        }
        if (randIndex <= mNumberOfLands) {
            mNumberOfCards--;
            mNumberOfLands--;
            return mNumberOfGoodLands.length + 1;
        }
        else {
            mNumberOfCards--;
            return mNumberOfGoodLands.length + 2;
        }
    }
}

final class RequirementsProcessorThread extends Thread {
    private final int NR_ITERATIONS = 1000000;
    private final Requirement[] mRequirements;
    private final Queue<String> mOutputQueue;

    public RequirementsProcessorThread(Queue<String> outputQueue, Requirement[] requirements) {
        mRequirements = requirements;
        mOutputQueue = outputQueue;
    }

    @Override
    public void run() {
        Deck deck = new Deck();
        for (Requirement requirement : mRequirements) {
            //Declare other variables
            int cardType; //Variable used to reference the type of card drawn from the deck
            int landsInHand; //This will describe the total amount of lands in your hand
            int goodLandsInHand; //This will describe the number of lands that can produce the right color in your hand
            int startingHandSize;
            boolean mulligan;

            if (requirement.colorRequirements.length == 1) {
                for (int nrGoodLands = requirement.colorRequirements[0]; nrGoodLands <= 17; nrGoodLands++) {
                    double countOk = 0.0; //This will be the number of relevant games where you draw enough lands and the right colored sources
                    double countConditional = 0.0; //This will be the number of relevant games where you draw enough lands
                    for (int i = 1; i <= NR_ITERATIONS; i++) {
                        //Draw opening Hand
                        mulligan = true;
                        startingHandSize = 8;
                        landsInHand = 0;
                        goodLandsInHand = 0;
                        while (mulligan && startingHandSize > 2) {
                            deck.resetDeck(requirement.numberOfLands, nrGoodLands, requirement.numberOfCards);
                            landsInHand = 0;
                            goodLandsInHand = 0;

                            for (int j = 1; j <= 7; j++) {
                                cardType = deck.DrawCard();
                                if (cardType < 3) {
                                    landsInHand++;
                                }
                                if (cardType == 1) {
                                    goodLandsInHand++;
                                }
                            }
                            mulligan = landsInHand < 2 || landsInHand > 5;
                            startingHandSize--;
                        }
                        if (startingHandSize < 7) {
                            landsInHand = Math.min(landsInHand, startingHandSize - 1);
                            goodLandsInHand = Math.min(goodLandsInHand, landsInHand);
                        }
                        //For turns 2 on, draw cards for the number of turns available
                        for (int turn = 2; turn <= requirement.cmc; turn++) {
                            cardType = deck.DrawCard();
                            if (cardType < 3) {
                                landsInHand++;
                            }
                            if (cardType == 1) {
                                goodLandsInHand++;
                            }
                        }

                        if (goodLandsInHand >= requirement.colorRequirements[0] && landsInHand >= requirement.cmc) {
                            countOk++;
                        }
                        if (landsInHand >= requirement.cmc) {
                            countConditional++;
                        }
                    }
                    mOutputQueue.add(requirement.colorRequirements[0] + ",0," + requirement.cmc + "," + nrGoodLands + ",0,0," + requirement.numberOfLands + "," + requirement.numberOfCards + "," + countOk / countConditional);
                }
            } else if (requirement.colorRequirements.length == 2) {
                for (int nrLandA = 0; nrLandA <= requirement.numberOfLands; nrLandA++) {
                    for (int nrLandB = 0; nrLandB <= requirement.numberOfLands - nrLandA; nrLandB++) {
                        for (int nrLandAB = 0; nrLandAB <= requirement.numberOfLands - nrLandA - nrLandB; nrLandAB++) {
                            double countOk = 0.0; //This will be the number of relevant games where you draw enough lands and the right colored sources
                            double countConditional = 0.0; //This will be the number of relevant games where you draw enough lands
                            for (int i = 1; i <= NR_ITERATIONS; i++) {
                                //Draw opening Hand
                                mulligan = true;
                                startingHandSize = 8;
                                landsInHand = 0;
                                int[] inHand = new int[6];
                                while (mulligan && startingHandSize > 2) {
                                    deck.resetDeck(requirement.numberOfLands, new int[]{nrLandA, nrLandB, nrLandAB}, requirement.numberOfCards);
                                    landsInHand = 0;
                                    inHand = new int[6];
                                    for (int j = 1; j <= 7; j++) {
                                        cardType = deck.DrawCard();
                                        if (cardType < 5) {
                                            landsInHand++;
                                        }
                                        inHand[cardType]++;
                                    }
                                    mulligan = landsInHand < 2 || landsInHand > 5;
                                    startingHandSize--;
                                }
                                if (startingHandSize < 7) {
                                    landsInHand = Math.min(landsInHand, startingHandSize - 1);
                                    int remaining = landsInHand - inHand[3];
                                    if (inHand[1] + inHand[2] > remaining) {
                                        int total = requirement.colorRequirements[0] + requirement.colorRequirements[1];
                                        inHand[1] = Math.min(inHand[1], remaining * requirement.colorRequirements[0] / total);
                                        inHand[2] = Math.min(inHand[2], remaining * requirement.colorRequirements[1] / total);
                                    }
                                }
                                //For turns 2 on, draw cards for the number of turns available
                                for (int turn = 2; turn <= requirement.cmc; turn++) {
                                    cardType = deck.DrawCard();
                                    if (cardType < 5) {
                                        landsInHand++;
                                    }
                                    inHand[cardType]++;
                                }
                                int landsForA = Math.min(requirement.colorRequirements[0], inHand[1]);
                                int landsForB = Math.min(requirement.colorRequirements[1], inHand[2]);
                                int landsNeeded = requirement.colorRequirements[0] + requirement.colorRequirements[1];
                                if (inHand[3] >= landsNeeded - landsForA - landsForB && landsInHand >= requirement.cmc) {
                                    countOk++;
                                }
                                if (landsInHand >= requirement.cmc) {
                                    countConditional++;
                                }
                            }
                            mOutputQueue.add(requirement.colorRequirements[0] + "," + requirement.colorRequirements[1] + "," + requirement.cmc + "," + nrLandA + "," + nrLandB + "," + nrLandAB + "," + requirement.numberOfLands + "," + requirement.numberOfCards + "," + countOk / countConditional);
                        }
                    }
                }
            }
        }
    }
}

public class HowManySources {
    private static final int[] deckSizes = new int[]{40, 60, 99};

    private static List<Requirement> getRequirements() {
        List<Requirement> requirements = new ArrayList<>();
        for (int nrCards : deckSizes) {
            int nrLands = 17 * nrCards / 40;
            for (int turnAlllowed = 1; turnAlllowed <= 15; turnAlllowed++) {
                for (int nrGoodLandsNeeded = 1; nrGoodLandsNeeded <= Math.min(6, turnAlllowed); nrGoodLandsNeeded++) {
                    requirements.add(new Requirement(nrCards, nrLands, turnAlllowed, new int[]{nrGoodLandsNeeded}));
                }
                for (int i = 1; i <= Math.min(3, turnAlllowed); i++) {
                    for (int j = 1; j <= Math.min(3, turnAlllowed - i); j++) {
                        requirements.add(new Requirement(nrCards, nrLands, turnAlllowed, new int[]{i, j}));
                        for (int k = 1; k <= Math.min(3, turnAlllowed - i - k); k++) {
                            requirements.add(new Requirement(nrCards, nrLands, turnAlllowed, new int[]{i, j, k}));
                        }
                    }
                }
            }
        }
        return requirements;
    }

    private static void getResults(List<Requirement> requirements) {
        int cores = Runtime.getRuntime().availableProcessors();
        Queue<String> outputQueue = new ConcurrentLinkedQueue<>();
        Thread outputThread = new Thread(() -> {
            while (true) {
                String str = outputQueue.poll();
                if (str == null) continue;
                if (str.equals("exit")) return;
                System.out.println(str);
            }
        });
        ExecutorService pool = Executors.newFixedThreadPool(cores);
        pool.execute(outputThread);
        for (Requirement requirement : requirements) {
            pool.execute(new RequirementsProcessorThread(outputQueue, new Requirement[] { requirement }));
        }
        outputQueue.add("exit");
        pool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Number of LandA Needed,Number of LandB Needed,CMC,Number Of LandA in Deck,Number of LandB in Deck,Number of LandAB in Deck,Number of Lands In Deck,Number of Cards in Deck,OnCurveProbability");
        List<Requirement> requirements = getRequirements();
        getResults(requirements);
    }
}
