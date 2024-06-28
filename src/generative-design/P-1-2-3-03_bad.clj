
(ns generative-design.P-1-2-3-03-bad
  [:require [quil.core :as q]
   [quil.middleware :as m]])

(def colorCount 20)
(def alphaValue 75)

(defn random-vector-len-n
  ([val n] (take n (repeatedly #(q/random val))))
  ([min max n] (take n (repeatedly #(q/random min max)))))
(defn constant-vector-len-n [val n] (vec (repeat n val)))
(defn interleave-2-vectors [v1 v2] (mapcat vector v1 v2))

(defn setup []
  (q/frame-rate 10)
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke))

(defn ensure-vector [input]
  (println input)
  (cond
    (float? input) [input] ; If input is a float, wrap it in a vector
    (vector? input) input
    (list? input) input; If input is already a vector, return it as is
    :else (throw (IllegalArgumentException. "Input must be either a float or a vector"))))

(defn get-parts [i]
  (let [partCount (+ i 1)
        mask (repeatedly partCount #(< (q/random 1) 0.075))
        fragments (repeatedly partCount #(q/random 2 20))
        generator #(q/random 2)
        parts (map #(repeatedly % generator) fragments)
        else-branch (repeatedly partCount #(q/random 2))
        params (map vector mask parts else-branch)
        _ (println parts)
        __ (println else-branch)]
    (mapcat (fn [[mask-i parts-i else-branch-i]]
              (if mask-i (ensure-vector parts-i) (ensure-vector else-branch-i))) params)))



;(defn gradient [x y w h c1 c2]
;  (let [ctx (q/get-sketch-by-id "sketch")
;        grd (.createLinearGradient ctx x y (+ x w) y)]
;    (.addColorStop grd 0 (.toString c1))
;    (.addColorStop grd 1 (.toString c2))
;    (.setFillStyle ctx grd)
;    (.fillRect ctx x y w h)))

(defn gradient-v2 [x y w h c1 c2]
  (q/begin-shape)
  (q/fill c1)
  (q/vertex x y)
  (q/vertex (+ x w) y)
  (q/fill c2)
  (q/vertex (+ x w) (+ y h))
  (q/vertex x (+ y h))
  (q/end-shape))

(defn draw-state [state]
  (let
   [noLoop (q/no-loop)
    background (q/background 0)
    randomSeed (q/random-seed (q/random 100000))
    hueValues (interleave-2-vectors (random-vector-len-n 360 colorCount) (constant-vector-len-n 195 colorCount))
    saturationValues (interleave-2-vectors (constant-vector-len-n 100 colorCount) (random-vector-len-n 100 colorCount))
    brightnessValues (interleave-2-vectors (random-vector-len-n 100 colorCount) (constant-vector-len-n 100 colorCount))
    rowCount (int (q/random 5 30))
    rowHeight (/ (q/height) rowCount)
    index (range rowCount -1 -1)
    _ (println index)
    parts_ (map get-parts index)
    __ (println parts_)
    counts (reductions + (map #(count %) parts_))
    params (map vector index parts_ counts)
    draw-row (fn  [i parts end-count]
               (let [sumPartsTotal (reduce + parts)
                     sumPartsNow (reductions + parts)
                     N (count parts)
                     counter (range (- end-count (count parts)) end-count)
                     index (map #(mod % colorCount) counter)
                     x (map #(q/map-range % 0 sumPartsTotal 0 (q/width)) sumPartsNow)
                     y (repeatedly N #(* i rowHeight))
                     w (map #(- (q/map-range % 0 sumPartsTotal 0 (q/width))) parts)
                     h (repeatedly N #(* 1.5 rowHeight))
                     col1  (repeatedly N   #(q/color 0))
                     col2 (map #(q/color (nth hueValues %) (nth saturationValues %) (nth brightnessValues %) alphaValue) index)
                     row-params (map vector x y w h col1 col2)]
                 (doseq [[x y w h col1 col2] row-params] (gradient-v2 x y w h col1 col2))))]
    (doseq [[i parts end-count] params] (draw-row i parts end-count))))


(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_03.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_03.jpg")))


(defn mouse-released [_ __]
  (q/start-loop))

(q/defsketch sketch
  :title "test"
  :size [800 800]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed
  :mouse-released mouse-released)


