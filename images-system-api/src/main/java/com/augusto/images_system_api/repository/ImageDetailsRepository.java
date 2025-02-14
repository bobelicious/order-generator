package com.augusto.images_system_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.augusto.images_system_api.model.ImageDetails;

public interface ImageDetailsRepository extends JpaRepository<ImageDetails, Long>  {
  Optional<ImageDetails>findByUserId(String userId);
}