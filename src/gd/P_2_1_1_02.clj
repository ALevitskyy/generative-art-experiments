(ns gd.P-2-1-1-02
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def width 600)
(def tile-count 20)
(def random-seed (atom 0))
(def alpha-left (atom 255))
(def alpha-right (atom 255))
(def color-left (atom nil))
(def color-right (atom nil))
(def black (atom nil))

(defn setup []
  (q/stroke-cap :round)
  (reset! color-left (q/color 197 0 123 @alpha-left))
  (reset! color-right (q/color 87 35 129 @alpha-right))
  (reset! black (q/color 0 0 0 255)))

(defn colors-equal [c1 c2]
  (and (= (q/red c1) (q/red c2))
       (= (q/green c1) (q/green c2))
       (= (q/blue c1) (q/blue c2))))


(defn update-coloralpha-atoms [color-atom alpha-atom]
  (if (= @alpha-atom 255)
    (do (reset! alpha-atom 127)
        (reset! color-atom (q/color
                            (q/red @color-atom) (q/green @color-atom) (q/blue @color-atom) @alpha-atom)))
    (do (reset! alpha-atom 255)
        (reset! color-atom (q/color
                            (q/red @color-atom) (q/green @color-atom) (q/blue @color-atom) @alpha-atom)))))

(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P-2-1-1-02.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P-2-1-1-02.jpg")
    :1 (q/stroke-cap :round)
    :2 (q/stroke-cap :square)
    :3 (q/stroke-cap :project)
    :4 (if (colors-equal @color-left @black)
         (reset! color-left (q/color 197 0 123 @alpha-left))
         (reset! color-left (q/color 0 0 0 @alpha-left)))
    :5 (if (colors-equal @color-right @black)
         (reset! color-right (q/color 87 35 129 @alpha-right))
         (reset! color-right (q/color 0 0 0 @alpha-right)))
    :6 (update-coloralpha-atoms color-left alpha-left)
    :7 (update-coloralpha-atoms color-right alpha-right)
    :0 (do
         (reset! alpha-left 255)
         (reset! alpha-right 255)
         (reset! color-left (q/color 0 0 0 @alpha-left))
         (reset! color-right (q/color 0 0 0 @alpha-right)))

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
      (if toggle
        (do
          (q/stroke @color-left)
          (q/stroke-weight (/ (q/mouse-x) 20))
          (q/line posX posY (+ posX (/ width tile-count)) (+ posY (/ width tile-count))))
        (do
          (q/stroke @color-right)
          (q/stroke-weight (/ (q/mouse-y) 20))
          (q/line posX (+ posY (/ width tile-count)) (+ posX (/ width tile-count)) posY))))))


(defn mouse-released [_ __] 0
  (reset! random-seed (int (q/random 100000))))



(def sketch (atom nil))
(defn run-sketch []
  (q/defsketch sketch
    :host "sketch-canvas"
    :title "P-2-1-1-02"
    :size [width width]
    :setup setup
    :draw draw-state
    :middleware [m/fun-mode]
    :key-pressed key-pressed
    :mouse-released mouse-released))

(run-sketch)

