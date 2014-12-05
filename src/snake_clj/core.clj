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

(defn- move-right [world [x y]]
  (let [y (mod y (arity-y world))
        x (mod (inc x) (arity-x world))]
    [x y]))

(defn- is-tail? [snake p]
  (some #(= p %) snake))

(defn right-of [{world :world snake :snake}]
  (if world
    (let [head-position (head snake)
          new-head-position (move-right world head-position)]
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