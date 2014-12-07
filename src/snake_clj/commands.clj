(ns snake-clj.commands
  (:require
    [snake-clj.core :refer :all]
    [snake-clj.events :as evt]
    [snake-clj.db :as db]))


(defn start-game! [id seed]
  (db/store! (evt/game-started id seed)))

(defn- move [direction-of event-factory id]
  (when-let [snake (db/load-aggregate id)]
    (let [turned-evt (event-factory id)
          hit-smthing-evt (cond
                            (wall? (direction-of snake)) (evt/hit-wall id)
                            (apple? (direction-of snake)) (evt/ate-apple id)
                            (tail? (direction-of snake)) (evt/ate-tail id))]
      (if (nil? hit-smthing-evt)
        [turned-evt]
        [turned-evt hit-smthing-evt]))))

(defn turn-right! [id]
  (println "turn right")
  (apply db/store! [(evt/turned-right id)]))

(defn turn-left! [id]
  (println "turn left")
  (apply db/store! [(evt/turned-left id)]))

(defn go-ahead! [id]
  (apply db/store!
         (move ahead-of evt/gone-ahead id)))