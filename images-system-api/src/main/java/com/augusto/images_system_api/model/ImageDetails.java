package com.augusto.images_system_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "image_details")
public class ImageDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_image", unique = true)
    private String imageName;
    @Column(name = "user_id")
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "usuarios_pkey"))
    private String userId;
    @Column(name = "product_id")
    @JoinColumn(name = "product_id",
            foreignKey = @ForeignKey(name = "produtos_pkey"), referencedColumnName = "id")
    private String productId;
    private String origin;
    private String createdAt;

    public ImageDetails(String imageName, String userId, String origin, String createdAt) {
        this.imageName = imageName;
        this.userId = userId;
        this.origin = origin;
        this.createdAt = createdAt;
    }

}
