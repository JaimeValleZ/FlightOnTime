package com.alura.flight_prediction.repository;


import com.alura.flight_prediction.entity.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {

    //Total de vuelos
    @Query("""
           SELECT COUNT(v)
           FROM Vuelo v
           WHERE v.aerolinea = :aerolinea
           """)
    long countTotalByAeorlinea(@Param("aerolinea") String aerolinea);

    //Total por el tipo de previision y aerolinea
    @Query("""
           SELECT COUNT(v)
           FROM Vuelo v 
           WHERE v.aerolinea = :aerolinea
           AND v.prevision = :prevision
           """)
    long countByAeorlineaAndPrevision(
      @Param("aerolinea") String aerolinea,
      @Param("prevision") String prevision
    );
}
