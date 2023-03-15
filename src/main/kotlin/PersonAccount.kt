data class PersonAccount(val id:Int,val login:String, val password:String){
    override fun equals(other: Any?) =
        other is PersonAccount && ((id == other.id && login == other.login &&
                (password == other.password )))
}
val MY_PERSON_ACCOUNT = PersonAccount(Int.MAX_VALUE,"bosyou", "iamnoyoi3.33")