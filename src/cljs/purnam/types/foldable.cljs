(ns purnam.types.foldable
  (:require
    [purnam.types.clojure :refer [obj-only]]
    [purnam.protocols :refer [Foldable fold foldmap op id]])
  (:use-macros [purnam.types.macros :only [extend-all]]))

(defn fold-array [fd] (fd))

(defn foldmap-array [fd g] (g (deref fd)))
  
(defn fold-atom [fd] (deref fd))

(defn foldmap-atom [fd g] (g (deref fd)))

(defn fold-coll [fd]
  (let [ide (id (first fd))]
    (reduce op ide fd)))

(defn foldmap-coll [fd g]
  (fold (map g fd)))

(defn fold-map [fd]
  (fold-coll (vals fd)))

(defn foldmap-map [fd g]
  (foldmap-coll (seq fd) g))

(extend-type nil Foldable
  (fold [_] nil)
  (foldmap [_ _] nil))

(extend-all Foldable
 [(fold [fd] (?% fd))
  (foldmap [fd g] (?% fd g))]
 
 ;;object            []
 array             [fold-array foldmap-array]
 Atom              [fold-atom foldmap-atom]

 [EmptyList LazySeq
  IndexedSeq RSeq NodeSeq 
  ArrayNodeSeq List Cons
  ChunkedCons ChunkedSeq 
  KeySeq ValSeq Range 
  PersistentArrayMapSeq
  
  PersistentVector
  Subvec BlackNode 
  RedNode    

  PersistentHashSet
  PersistentTreeSet] [fold-coll foldmap-coll]

 [PersistentHashMap
  PersistentTreeMap
  PersistentArrayMap]  [fold-map foldmap-map])