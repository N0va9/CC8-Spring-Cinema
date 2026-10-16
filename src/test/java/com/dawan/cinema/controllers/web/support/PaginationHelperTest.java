package com.dawan.cinema.controllers.web.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 * Tests de l'algo de pagination compacte
 * Les pages sont affichées à partir de 1, "..." represente un trou
 */
public class PaginationHelperTest {

    private final PaginationHelper helper = new PaginationHelper();

    private List<String> render(int currentIndex, int totalPages) {
        Page<String> page = new PageImpl<>(List.of("x"), PageRequest.of(currentIndex, 1), totalPages);
        return helper.items(page).stream()
            .map(item -> item.ellipsis() ? "..." : String.valueOf(item.number() + 1))
            .toList();
    }

    @Test 
    void affiche_toutes_les_pages_quand_il_y_en_a_peu() {
        assertThat(render(0, 3)).containsExactly("1", "2", "3");
    }

     @Test
    void premiere_page_affiche_sa_voisine_puis_la_derniere() {
        assertThat(render(0, 360)).containsExactly("1", "2", "...", "360");
    }

    @Test
    void page_du_milieu_affiche_ses_voisines_entre_deux_trous() {
        assertThat(render(149, 360)).containsExactly("1", "...", "149", "150", "151", "...", "360");
    }

    @Test
    void un_trou_d_une_seule_page_affiche_le_numero_plutot_que_des_points() {
        assertThat(render(2, 360)).containsExactly("1", "2", "3", "4", "...", "360");
    }

    @Test
    void derniere_page_affiche_la_premiere_puis_sa_voisine() {
        assertThat(render(359, 360)).containsExactly("1", "...", "359", "360");
    }
    
}
