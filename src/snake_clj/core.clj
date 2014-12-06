(ns snake-clj.core)

(defn- arity-y [matrix]
  (count matrix))

(defn- arity-x [matrix]
  (count (nth matrix 0)))

(defn- get-at [matrix [x y]]
  (-> matrix
      (nth y)
      (nth x)))

(defn- head [snake]
  (peek snake))

(defn- move-right [world [x y] heading]
  (let [[xr yr] (condp = heading
                  :right [0 1]
                  :left [0 -1]
                  :up [1 0]
                  :down [-1 0])
        y (mod (+ y yr) (arity-y world))
        x (mod (+ x xr) (arity-x world))]
    [x y]))

(defn- is-tail? [snake p]
  (some #(= p %) snake))

(defn right-of [{ :keys [world snake heading] :as snake-state}]
  (if world
    (let [head (head snake)
          new-head-position (move-right world head heading)]
      (prn snake new-head-position world)
      (if (is-tail? snake new-head-position)
        :tail
        (get-at world new-head-position)))
    nil))

(defn left-of [])

(defn ahead-of [])

(defn apple? [cell])

(defn wall? [cell])

(defn tail? [cell])

(defn snake-state
  "factory method that returns a snake state"
  [world snake heading]
  {:pre [(> (arity-x world) 0)
         (> (arity-y world) 0)
         (vector? snake)
         (#{:up :down :right :left} heading)]}
  {:world world :snake snake :heading heading})