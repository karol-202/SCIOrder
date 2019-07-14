package pl.karol202.sciorder.client.js.common.util

import kotlinx.css.RuleSet
import styled.StyledBuilder
import styled.css

fun StyledBuilder<*>.overrideCss(handler: RuleSet) = css { specific { handler() } }
