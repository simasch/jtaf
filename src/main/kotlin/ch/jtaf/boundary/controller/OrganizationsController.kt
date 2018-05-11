package ch.jtaf.boundary.controller

import ch.jtaf.boundary.controller.Views.ORGANIZATIONS
import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class OrganizationsController(private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/organizations")
    fun get(@AuthenticationPrincipal user: User, model: Model): String {
        model["organizations"] = organizationRepository.findAllByOwner(user.username)
        return ORGANIZATIONS
    }

}
