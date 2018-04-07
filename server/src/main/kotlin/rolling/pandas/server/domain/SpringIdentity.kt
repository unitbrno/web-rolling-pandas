package rolling.pandas.server.domain

data class SpringIdentity(val userName: String, val authenticated: Boolean, val tokenInfo: Map<String, String> = emptyMap())