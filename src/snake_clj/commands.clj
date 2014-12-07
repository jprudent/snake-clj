(ns snake-clj.commands
  (:require
    [snake-clj.core :refer :all]
    [snake-clj.events :as evt]
    [snake-clj.db :as db]))

(defn start-game! [id seed]
  (db/store! (evt/game-started id seed)))

(defn go-ahead! [id]
  (apply db/store!
         (if-let [snake (db/load-aggregate id)]
           (if (:alive? snake)
             (let [turned-evt (evt/gone-ahead id)
                   hit-smthing-evt (cond
                                     (wall? (ahead-of snake)) (evt/hit-wall id)
                                     (apple? (ahead-of snake)) (evt/ate-apple id)
                                     (and (tail? (ahead-of snake))) (evt/ate-tail id))]
               (if (nil? hit-smthing-evt)
                 [turned-evt]
                 [turned-evt hit-smthing-evt])))
           [])))

(defn turn-left! [id]
  (println "turn left")
  (if (:alive? (db/load-aggregate id))
    (do
      (apply db/store! [(evt/turned-left id)])
      (go-ahead! id))))

(defn turn-right! [id]
  (println "turn right")
  (if (:alive? (db/load-aggregate id))
    (do
      (apply db/store! [(evt/turned-right id)])
      (go-ahead! id))))