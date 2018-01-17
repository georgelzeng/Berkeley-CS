# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


import mdp, util

from learningAgents import ValueEstimationAgent
import collections

class ValueIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A ValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100):
        """
          Your value iteration agent should take an mdp on
          construction, run the indicated number of iterations
          and then act according to the resulting policy.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state, action, nextState)
              mdp.isTerminal(state)
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = util.Counter() # A Counter is a dict with default 0
        self.runValueIteration()

    def runValueIteration(self):
        # Write value iteration code here

        for i in range(self.iterations):
          nextIter = util.Counter()
          for state in self.mdp.getStates():
            if self.mdp.isTerminal(state):
              nextIter[state] = 0
              continue;
            maxAction = float("-inf")
            for action in self.mdp.getPossibleActions(state):
              actionVal = 0
              for transition, prob in self.mdp.getTransitionStatesAndProbs(state, action):
                actionVal += prob * (self.mdp.getReward(state, action, transition) + 
                  self.discount * self.values[transition])
              maxAction = max(maxAction, actionVal)
            nextIter[state] = maxAction
          self.values = nextIter


    def getValue(self, state):
        """
          Return the value of the state (computed in __init__).
        """
        return self.values[state]


    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.values.
        """
        value = 0
        for transition, prob in self.mdp.getTransitionStatesAndProbs(state, action):
          value += prob * (self.mdp.getReward(state, action, transition) + self.discount * self.values[transition])
        return value


    def computeActionFromValues(self, state):
        """
          The policy is the best action in the given state
          according to the values currently stored in self.values.

          You may break ties any way you see fit.  Note that if
          there are no legal actions, which is the case at the
          terminal state, you should return None.
        """
        if self.mdp.isTerminal(state):
          return None
        else:
          policy = None
          value = float("-inf")
          for action in self.mdp.getPossibleActions(state):
            if self.computeQValueFromValues(state, action) > value:
              value = self.computeQValueFromValues(state, action)
              policy = action
          return policy


    def getPolicy(self, state):
        return self.computeActionFromValues(state)

    def getAction(self, state):
        "Returns the policy at the state (no exploration)."
        return self.computeActionFromValues(state)

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)

class AsynchronousValueIterationAgent(ValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        An AsynchronousValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs cyclic value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 1000):
        """
          Your cyclic value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy. Each iteration
          updates the value of only one state, which cycles through
          the states list. If the chosen state is terminal, nothing
          happens in that iteration.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state)
              mdp.isTerminal(state)
        """
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        for i in range(self.iterations):
          nextIter = self.values
          index = i % len(self.mdp.getStates())
          state = self.mdp.getStates()[index]
          if self.mdp.isTerminal(state):
            # nextIter[state] = 0
            continue;
          maxAction = float("-inf")
          for action in self.mdp.getPossibleActions(state):
            actionVal = 0
            for transition, prob in self.mdp.getTransitionStatesAndProbs(state, action):
              actionVal += prob * (self.mdp.getReward(state, action, transition) + 
                self.discount * self.values[transition])
            maxAction = max(maxAction, actionVal)
          # nextIter[state] = maxAction
          self.values[state] = maxAction

class PrioritizedSweepingValueIterationAgent(AsynchronousValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A PrioritizedSweepingValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs prioritized sweeping value iteration
        for a given number of iterations using the supplied parameters.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100, theta = 1e-5):
        """
          Your prioritized sweeping value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy.
        """
        self.theta = theta
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):

      #helper
      def computeQValue(state):
        return max(self.computeQValueFromValues(state, action) for action in self.mdp.getPossibleActions(state))
      
      #intialize pq
      pq = util.PriorityQueue()
      #compute predecessors
      preds = {}
      for state in self.mdp.getStates():
        if self.mdp.isTerminal(state):
          continue;
        for action in self.mdp.getPossibleActions(state):
          for transition, prob in self.mdp.getTransitionStatesAndProbs(state, action):
            if prob > 0.0:
              if transition not in preds:
                preds[transition] = set()
              preds[transition].add(state)

      #add shit to pq
      for state in self.mdp.getStates():
        if self.mdp.isTerminal(state):
          continue;
        qVal = computeQValue(state)
        diff = abs(self.values[state] - qVal)
        pq.push(state, -diff)

      #iterate through pq
      for i in range(self.iterations):
        if pq.isEmpty():
          break;
        state = pq.pop()
        self.values[state] = computeQValue(state)

        #iterate through predecessors
        for pred in preds[state]:
          predDiff = abs(self.values[pred] - computeQValue(pred))
          if predDiff > self.theta:
            # if pred in pq:
            #   if -diff < pq.getPriority(pred):
            pq.update(pred, -predDiff)


