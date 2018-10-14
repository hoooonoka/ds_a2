============================= part 1 =========================================

My name is XX, I am going to give a brief presentation introducing our pacman project.

In the project, we have used 3 method and built 3 different agents. They are approximate q learning agent, mcts-only agent & mcts-search agent

we use approximate q learning in approximate q learning agent.

For the mcts-only agent, we use mcts to build the pacman agent.

For the mcts-search agent, we use mcts method, combine with search method to build our pacman agent.

The mcts-search agent is our finnal submitted agent.


challenge
One challenge we faced during the implementation is building proper evaluation function for our Monte Carlo Tree Search agent. It is essential for our Monte Carlo Tree Search agent as we cannot run the whole game process in simulation phase and hence require some evaluation function to tell us if the new state is good enough. It is hard to find proper features and combine them in a good way to evaluate the new states. We tried to observe the game and used some domin knowledge when constructing and improving our evaluation function. Although it still not performing well enough, it performed better comparing to our early agent and could get a high rank position in pre-contests.

============================= part 2 =========================================

improvement
Currently we select and expand node randomly in expansion phase. One possible improvement we could make in the future is that we can use UCB1 method to select node to expand. This might make our agent's performance better. This method we have not implement in our agent now, as this might make the coding harder and we do not have much time for this.

Another possible improvement we could make in the future is that we can use some techniques to inform opponents' action in our defensive agent. Actually simply chasing opponent agent is not a good idea as the opponent pacman can often run away easily. It might be a good idea to inferring opponent's action and position when defending our food. However, this might be quite hard to build such a good model. Maybe we should involve some technique from Markov Decision Processes, Q Learning or Plan Recognition techniques.


============================= part 3 =========================================

This video records our approximate q learning agent play aginst baseline agent. The red part is our approximate q learning agent and blue part is baseline agent.

approximate q learning agent: eat efficiently when there is no enemy around

conservative when there are enemy around and stop eating food

easily to be caught and eaten by enemy



This video records our mcts-only agent play aginst our scts-search agent. The red part is our mcts-only agent and blue part is mcts-search agent.

mcts-only agent: hard to be caught by enemy

conservative when there are enemy around

return home frequently


mcts-search agent:

similar to mcts-only agent

return home less frequently

better simulation method help it be less conservative and still alway escape from enemy
