object DataTypes {
  type Extent = Vector[Int]
  type Intent = Vector[Int]
  type FormalContext = Vector[Intent]
  case class MetaData(intents: Map[Int, String], extents: Map[Int, String])
  case class FormalConcept(objs: Extent, attrs: Intent)
}

object FCA {
  def main(args: Array[String]): Unit = {
    import DataTypes.MetaData
    // from exercise sheet 8, all indices -1!
    val fc = Vector(
      Vector(1,3), Vector(1,3), Vector(1,2),
      Vector(0,2), Vector(0,2), Vector(1,3),
      Vector(1,2), Vector(1,2), Vector(1,3),
      Vector(0,2))
    val meta = MetaData(
      Map(0 -> "< 60", 1 -> ">= 60", 2 -> "1. Amtszeit", 3-> "2. Amtszeit"),
      Map(0 -> "Heuss", 1 -> "Lübke", 2 -> "Heinemann",
        3 -> "Scheel", 4 -> "Carstens", 5 -> "Weizsäcker",
        6 -> "Herzog", 7 -> "Rau", 8 -> "Köhler", 9 -> "Wulff")
    )
    //val concepts = NextClosure.runNextClosure(fc)
    //println("Concepts: " + concepts)

    // should be: Vector(2, 3, 4, 6, 7, 9)
    println("Extends: " + NextClosure.computePrimeFromI(fc, Vector(2)))
    // should be: Vector(2)
    println("Intents: " + NextClosure.computePrime(fc, Vector(2,3,4,6,7,9)))
    // should be: Vector()
    println("Extends: " + NextClosure.computePrimeFromI(fc, Vector(0, 1)))
    // should be: Vector(0, 1, 2, 3)
    println("Intents: " + NextClosure.computePrime(fc, Vector()))

    println("Next Closure: " + NextClosure.runNextClosure(fc, meta))
  }
}

/* Implementation assumes that we start from index 0, it also closely
 * follows the table structure from the lecture */
object NextClosure {
  import DataTypes._

  /* TODO: handle empty intent */
  def computePrimeFromI(fc: FormalContext, intent: Intent): Extent = {
    if (intent.isEmpty)
      (0 until (fc.size)).toVector
    else {
      val intentSet = intent.toSet
      val intentSetSize = intentSet.size
      fc.zipWithIndex.filter{ case (l, idx) => (l.toSet & intentSet).size == intentSetSize }.map(_._2)
    }
  }

  def computePrime(fc: FormalContext, extent: Extent): Intent = {
    // this could just use the meta information
    if (extent.isEmpty)
      (0 to fc.foldLeft(0)( (max, l) => if (max > l.sorted.last) max else l.sorted.last )).toVector
    else
      fc.zipWithIndex.filter{ case (_, idx) => extent.contains(idx) }.map(_._1.toSet).reduce(_ & _).toVector
  }

  def computeClosure(fc: FormalContext, intent: Intent): Intent = {
    val extent = computePrimeFromI(fc, intent)
    computePrime(fc, extent)
  }

  // step 3
  def growIntent(intent: Intent, i: Int): Intent = {
    i +: intent.filter(_ < i)
  }

  /* See page 50 */
  /* TODO: use scalaz equality here */
  def cmp(a: Intent, b: Intent, idx: Int): Boolean = {
    if (b.contains(idx) && !a.contains(idx))
      a.filter(_ < idx) == b.filter(_ < idx)
    else
      false
  }

  def runNextClosure(fc: FormalContext, meta: MetaData): Vector[Intent] = {
    // very first step: empty intent
    var intents: Vector[Intent] = Vector(Vector.empty)
    // TODO: replace with M
    val attrCnt: Int = meta.intents.size
    val m: Intent = Vector.range(0, attrCnt)

    println("Starting NEXT CLOSURE...\n")
    println(s"M: $m")
    println(s"Number of intents: $attrCnt\n")

    while (intents.head != m) {
      def go(i: Int): Unit = {
        val curIntent = intents.head

        // skip i if attribute at position i is contained in the intent
        if (!curIntent.contains(i)) {
          val grownIntent = growIntent(curIntent, i)
          val closure = computeClosure(fc, grownIntent)
          if ( cmp(curIntent, closure, i) ) {
            intents = closure +: intents
            println(s"$curIntent\t| $i\t| ${grownIntent.sorted}\t | $closure\t| X")
          } else {
            println(s"$curIntent\t| $i\t| ${grownIntent.sorted}\t | $closure\t| ")
            go(i - 1)
          }
        } else
          go(i - 1)
      }
      go(attrCnt - 1)
    }

    intents
  }
}


object Titanic {
  type Attr = Int
  case class AttrSet(set: Set[Attr], suppport: Int, prevSupport: Int)
  def isFrequent(intent: AttrSet, objCount: Int, minsupp: Double): Boolean = support(intent, objCount) > minsupp

  // (a,b), (b,c)
  def genNewSet(a: Set[Attr], b: Set[Attr]): Set[Attr] = {
    val size = a.size
    val diff = a &~ b
    ???
  }

  def genCandidates(attrSets: Vector[AttrSet], k: Int): Vector[AttrSet] = {
    // previous iteration index
    val p = k - 1
    var newSet = Vector[AttrSet]()

    ???
  }

  /* supp(X) = X' / G' */
  def support(intent: AttrSet, objectCount: Int): Double = {
    ???
  }

}
