(ns gd.P-2-1-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def width 600)
(def tile-count 20)



(def random-seed (atom 0))

(defn setup []
  (q/stroke-cap :round))

(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P-2-1-1-01.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P-2-1-1-01.jpg")
    :1 (q/stroke-cap :round)
    :2 (q/stroke-cap :square)
    :3 (q/stroke-cap :project)
    (println key)))

(defn draw-state [state]
  (q/background 255)
  (q/random-seed @random-seed)
  (doseq [gridY (range tile-count)
          gridX (range tile-count)]
    ; Your code here, e.g., print gridX and gridY
    (let [posX (* gridX (/ width tile-count))
          posY (* gridY (/ width tile-count))
          toggle (< (q/random 1) 0.5)]

      (if toggle (q/stroke-weight (/ (q/mouse-x) 20)) (q/stroke-weight (/ (q/mouse-y) 20)))
      (if toggle
        (q/line posX posY (+ posX (/ width tile-count)) (+ posY (/ width tile-count)))
        (q/line posX (+ posY (/ width tile-count)) (+ posX (/ width tile-count)) posY)))))


(defn mouse-released [_ __]
  (reset! random-seed (int (q/random 100000))))

(def sketch (atom nil))
(defn run-sketch []
  (q/defsketch sketch
    :host "sketch-canvas"
    :title "P-2-1-1-01"
    :size [width width]
    :setup setup
    :draw draw-state
    :middleware [m/fun-mode]
    :key-pressed key-pressed
    :mouse-released mouse-released))

