package arrow.instances

import arrow.Kind
import arrow.core.Eval
import arrow.data.*
import arrow.instance
import arrow.typeclasses.*

@instance(SetK::class)
interface SetKSemigroupInstance<A> : Semigroup<SetK<A>> {
    override fun SetK<A>.combine(b: SetK<A>): SetK<A> = (this + b).k()
}

@instance(SetK::class)
interface SetKMonoidInstance<A> : SetKSemigroupInstance<A>, Monoid<SetK<A>> {
    override fun empty(): SetK<A> = emptySet<A>().k()
}

@instance(SetK::class)
interface SetKEqInstance<A> : Eq<SetK<A>> {

    fun EQ(): Eq<A>

    override fun SetK<A>.eqv(b: SetK<A>): Boolean =
            if (size == b.size) set.map { aa ->
                b.find { bb -> EQ().run { aa.eqv(bb) } } != null
            }.fold(true) { acc, bool ->
                acc && bool
            }
            else false

}

@instance(SetK::class)
interface SetKShowInstance<A> : Show<SetK<A>> {
    override fun SetK<A>.show(): String =
            toString()
}

@instance(SetK::class)
interface SetKFoldableInstance : Foldable<ForSetK> {
    override fun <A, B> foldLeft(fa: SetKOf<A>, b: B, f: kotlin.Function2<B, A, B>): B =
            fa.fix().foldLeft(b, f)

    override fun <A, B> foldRight(fa: SetKOf<A>, lb: Eval<B>, f: kotlin.Function2<A, Eval<B>, Eval<B>>): Eval<B> =
            fa.fix().foldRight(lb, f)

    override fun <A> Kind<ForSetK, A>.isEmpty(): kotlin.Boolean =
            fix().isEmpty()
}

@instance(SetK::class)
interface SetKSemigroupKInstance : SemigroupK<ForSetK> {
    override fun <A> Kind<ForSetK, A>.combineK(y: Kind<ForSetK, A>): SetK<A> =
            fix().combineK(y)
}

@instance(SetK::class)
interface SetKMonoidKInstance : MonoidK<ForSetK> {
    override fun <A> empty(): SetK<A> =
            SetK.empty()

    override fun <A> Kind<ForSetK, A>.combineK(y: Kind<ForSetK, A>): SetK<A> =
            fix().combineK(y)
}