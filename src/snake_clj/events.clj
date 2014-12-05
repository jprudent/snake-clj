(ns snake-clj.events)

(defn- event [type id]
  {:event-type type
   :id         id})

(defn game-started [id]
  (event :game-started id))

(defn hit-wall [id]
  (event :hit-wall id))

(defn ate-tail [id]
  (event :ate-tail id))

(defn ate-apple [id]
  (event :ate-apple id))

(defn turned-right [id]
  (event :turned-right id))

(defn turned-left [id]
  (event :turned-left id))

(defn gone-ahead [id]
  (event :gone-ahead id))