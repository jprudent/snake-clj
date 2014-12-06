(defproject snake-clj "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [org.clojure/data.generators "0.1.2"]
                           [com.badlogicgames.gdx/gdx "1.4.1"]
                           [com.badlogicgames.gdx/gdx-backend-lwjgl "1.4.1"]
                           [com.badlogicgames.gdx/gdx-box2d "1.4.1"]
                           [com.badlogicgames.gdx/gdx-box2d-platform "1.4.1"
                            :classifier "natives-desktop"]
                           [com.badlogicgames.gdx/gdx-bullet "1.4.1"]
                           [com.badlogicgames.gdx/gdx-bullet-platform "1.4.1"
                            :classifier "natives-desktop"]
                           [com.badlogicgames.gdx/gdx-platform "1.4.1"
                            :classifier "natives-desktop"]
                           [org.clojure/clojure "1.6.0"]
                           [play-clj "0.4.2"]]
            :source-paths ["src" "src-opengl" "desktop"]
            :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
            :aot [snake-clj.desktop.desktop-launcher]
            :main snake-clj.desktop.desktop-launcher)
