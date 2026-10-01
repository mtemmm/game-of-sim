# Game of SIM

A multi-threaded client-server application implementing the **Game of SIM**. Features configurable AI difficulty levels using **Minimax with Alpha-Beta pruning**, paired with comprehensive complexity analysis.

---

Game of SIM is a **positional game** played on the edges of a complete graph $K_6$. Two players take turns coloring uncolored edges with their respective color. 

* **Objective:** Avoid forming a monochromatic triangle ($K_3$) of your own color.
* By **Ramsey's theorem** ($R(3,3) = 6$), every complete graph $K_6$ colored with 2 colors *must* contain at least one monochromatic triangle and therefore, **draws are impossible** and a winner is guaranteed.
* **Game state space:** With 15 edges, the upper bound for state evaluations without pruning is $15! \approx 1.3 \times 10^{12}$ states. Alpha-Beta pruning along with heuristic evaluation reduces this drastically to ensure real-time response times.

---

## Key features

* **Multithreaded Client-Server architecture:** Built using Java Sockets. Server handles game state updates asynchronously via isolated `ClientHandler` threads to keep the UI smooth and non-blocking.
* **Distinct levels:**
  * **Very easy:** Random move selection.
  * **Medium / Hard:** Bounded-depth Minimax search with custom heuristic evaluation (evaluating edge risks).
  * **Impossible:** Full Alpha-Beta pruning search evaluating all reachable game states.
  * Also included both naive full-tree search and optimazed Alpha-Beta pruning implementations to conduct comparisons of execution time and space complexity.
* **Interactive GUI:** Created a clean Java Swing UI to visualize different levels, the graph edges and handle player moves.
* **Full theoretical complexity analysis:** Accompanied by a research paper evaluating theoretical time/space complexity alongside measured execution runtimes across all algorithms used.

---

## Tech stack

* **Language:** Java 17+
* **Networking & concurrency:** Multi-threaded client-server architecture using Java Sockets to keep the UI smooth during AI processing.
* **GUI:** Java Swing
* **Algorithms:** Minimax, Alpha-Beta pruning, custom heuristics

